package com.trzewik.spring

trait FileOperator {
    void saveFile(String text, String directoryPath = 'build/contracts/', String fileName = 'swagger.json') {
        def destination = new File(directoryPath)
        if (destination != null && !destination.exists()) {
            destination.mkdirs()
        }
        def file = new File(directoryPath, fileName)
        file.write(text)
    }
}
