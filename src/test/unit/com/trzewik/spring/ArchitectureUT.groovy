package com.trzewik.spring

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaAnnotation
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.Priority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.priority

class ArchitectureUT extends Specification {
    static final String BASE_PACKAGE = 'com.trzewik.spring'
    static final String DOMAIN_PACKAGE = "${BASE_PACKAGE}.domain"

    static final String[] PACKAGES_ALLOWED_IN_DOMAIN = [
        'org.slf4j..',
        'lombok..',
        'com.google.common..',
        'java..',
        "${DOMAIN_PACKAGE}.."
    ]

    @Shared
    ClassFileImporter importer = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
    @Shared
    JavaClasses allClasses = importer.importPackages(BASE_PACKAGE).as('ALL')
    @Shared
    JavaClasses domainClasses = importer.importPackages(DOMAIN_PACKAGE).as('DOMAIN')

    def 'in domain are allowed only classes from: java, common libraries and lombok'() {
        given:
            def rule = priority(Priority.HIGH).noClasses()
                .should().dependOnClassesThat().resideOutsideOfPackages(PACKAGES_ALLOWED_IN_DOMAIN)
        expect:
            rule.check(domainClasses)
    }

    @Unroll
    def 'in domain are not allowed #DESCRIPTION'() {
        given:
            def rule = classes().should()
                .notBeAnnotatedWith(getAnnotations(DESCRIPTION, PACKAGE_NAME))
        expect:
            rule.check(domainClasses)
        where:
            PACKAGE_NAME          | DESCRIPTION
            'org.springframework' | 'Spring annotations'
            'com.fasterxml'       | 'FasterXML annotations'
            'javax'               | 'JavaX annotations'
            'org.hibernate'       | 'Hibernate annotations'
            'com.vladmihalcea'    | 'Vladmihalcea annotations'
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

}
