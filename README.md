
# Pdf Printing

Pdf Printing is a Java project developed using Spring Boot that enables the generation of PDF files from HTML + CSS templates with support for dynamic data. The project utilizes the Velocity templating engine to facilitate the customization of documents generated at runtime.

## Features

- Generate PDF files from HTML + CSS templates.
- Support for dynamic data sent in the request.
- Utilizes the Velocity templating engine for greater flexibility in formatting.
- Easy and quick configuration through Spring Boot.

## Technologies Used

- **Java**: JDK 21
- **Spring Boot**: Framework for building Java applications.
- **Velocity**: Templating engine used to generate dynamic content in HTML.
- **Apache PDFBox**: Library for creating and manipulating PDF files.

## Prerequisites

- JDK 21 or higher
- Maven or Gradle for dependency management
- IDE (such as IntelliJ IDEA, Eclipse, or Spring Tool Suite)

## Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/your-username/pdf-printing.git
   ```

2. Navigate to the project directory:

   ```bash
   cd pdf-printing
   ```

3. Compile the project and install dependencies:

   For Maven:

   ```bash
   mvn clean install
   ```

   For Gradle:

   ```bash
   ./gradlew build
   ```

## Configuration

1. **application.properties**: Configure the application properties in `src/main/resources/application.properties` as needed.

2. **Templates**: Place your Velocity templates in the `src/main/resources/templates` directory.

## Usage

To generate a PDF file, make an HTTP POST request to the corresponding endpoint, passing the required data. An example request can be made using `curl`:

```bash
curl -X POST http://localhost:8080/api/pdf \
     -H "Content-Type: application/json" \
     -d '{\
           "documentTemplateName": "Template-1",\
           "dataMap": {\
               "paragrafo1": "This is the first paragraph.",\
               "paragrafo2": "This is the second paragraph.",\
               "paragrafo3": "This is the third paragraph."\
           }\
         }'
```

The server will process the request and generate a PDF file based on the specified template.

## Contribution

Contributions are welcome! Feel free to open issues or pull requests. For more details, please refer to the [CONTRIBUTING.md](CONTRIBUTING.md) file.

## License

This project is licensed under the [MIT License](LICENSE).


