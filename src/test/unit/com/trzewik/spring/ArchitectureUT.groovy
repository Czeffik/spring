package com.trzewik.spring

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaAnnotation
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.Priority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.Embeddable
import javax.persistence.Entity

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.priority

class ArchitectureUT extends Specification {
    static final String BASE_PACKAGE = 'com.trzewik.spring'
    static final String DOMAIN_PACKAGE = "${BASE_PACKAGE}.domain"
    static final String INFRASTRUCTURE_PACKAGE = "${BASE_PACKAGE}.infrastructure"
    static final String INTERFACES_PACKAGE = "${BASE_PACKAGE}.interfaces"

    static final String[] PACKAGES_ALLOWED_IN_DOMAIN = [
        'org.slf4j',
        'lombok',
        'com.google.common',
        'java'
    ]

    static final String[] PACKAGES_ALLOWED_IN_DOMAIN_MATCHER = PACKAGES_ALLOWED_IN_DOMAIN.collect { "${it}.." }

    @Shared
    ClassFileImporter importer = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
    @Shared
    JavaClasses allClasses = importer.importPackages(BASE_PACKAGE).as('ALL')
    @Shared
    JavaClasses domainClasses = importer.importPackages(DOMAIN_PACKAGE).as('DOMAIN')

    @Unroll
    def '#DOMAIN domain should use classes from allowed packages and #DOMAIN package'() {
        given:
            def domainPackage = "${DOMAIN_PACKAGE}.${DOMAIN}.."
        and:
            def rule = priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(domainPackage)
                .should().dependOnClassesThat().resideOutsideOfPackages(getAllowedPackages(domainPackage))
        expect:
            rule.check(domainClasses)
        where:
            DOMAIN   | _
            'game'   | _
            'player' | _
    }

    def 'in domain are only allowed annotations from: java, common libraries and lombok'() {
        given:
            def rule = classes().should()
                .notBeAnnotatedWith(
                    getOtherAnnotations(
                        "annotations from packages other than: $PACKAGES_ALLOWED_IN_DOMAIN",
                        PACKAGES_ALLOWED_IN_DOMAIN
                    )
                )
        expect:
            rule.check(domainClasses)
    }

    def 'domain fields should be final'() {
        given:
            def rule = fields().should().beFinal()
        expect:
            rule.check(domainClasses)
    }

    def 'all fields should be private'() {
        given:
            def rule = fields()
                .that().areDeclaredInClassesThat().areNotEnums()    //ENUM values are treated as fields
                .should().bePrivate()
        expect:
            rule.check(allClasses)
    }

    def 'all fields in entities should NOT be final'() {
        given:
            def rule = fields()
                .that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
                .or().areDeclaredInClassesThat().areAnnotatedWith(Embeddable.class)
                .should().notBeFinal()
        expect:
            rule.check(allClasses)
    }

    def 'persistence classes should be in infrastructure.db package'() {
        given:
            def rule = classes()
                .that().areAnnotatedWith(Entity.class)
                .or().areAnnotatedWith(Embeddable.class)
                .should().resideInAPackage("${INFRASTRUCTURE_PACKAGE}.db..")
        expect:
            rule.check(allClasses)
    }

    def 'controllers should be in interfaces.rest package'() {
        given:
            def rule = classes()
                .that().areAnnotatedWith(RestController.class)
                .or().areAnnotatedWith(Controller.class)
                .should().resideInAPackage("${INTERFACES_PACKAGE}.rest..")
        expect:
            rule.check(allClasses)
    }

    def 'configuration classes should end with Configuration'() {
        given:
            def rule = classes()
                .that().areAnnotatedWith(Configuration.class)
                .should().haveSimpleNameEndingWith('Configuration')
        expect:
            rule.check(allClasses)
    }

    def 'RestController classes should end with Controller'() {
        given:
            def rule = classes()
                .that().areAnnotatedWith(RestController.class)
                .should().haveSimpleNameEndingWith('Controller')
        expect:
            rule.check(allClasses)
    }

    @Unroll
    def 'Autowired annotations is not used on #DESCRIPTION'() {
        given:
            def rule = CODE_UNIT.should()
                .notBeAnnotatedWith(Autowired.class)
        expect:
            rule.check(allClasses)
        where:
            CODE_UNIT      || DESCRIPTION
            constructors() || 'constructors'
            fields()       || 'fields'
            methods()      || 'methods'
    }

    DescribedPredicate<JavaAnnotation> getAnnotations(String description, String packageName) {
        return new DescribedPredicate<JavaAnnotation>(description) {
            @Override
            boolean apply(JavaAnnotation input) {
                return input.rawType.packageName.startsWith(packageName)
            }
        }
    }

    DescribedPredicate<JavaAnnotation> getOtherAnnotations(String description, String[] packageNames) {
        return new DescribedPredicate<JavaAnnotation>(description) {
            @Override
            boolean apply(JavaAnnotation input) {
                return packageNames.any { packageName -> !input.rawType.packageName.startsWith(packageName) }
            }
        }
    }

    private static String[] getAllowedPackages(String domainPackage) {
        def copy = []
        copy.addAll(PACKAGES_ALLOWED_IN_DOMAIN_MATCHER)
        copy << domainPackage
        return copy
    }

}
