# Biblioteca
Sistema que permite gestionar una colección de libros y usuarios de una biblioteca digital.

## Motivación
Es una entrega para la actividad *Sistema de Gestión de una Biblioteca Digital*, de la asignatura de Programación del curso 1º del Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM).

## Cómo ejecutar el programa

### Pre-requisitos

- Tener Java 21 instalado o un IDE que venga con su propia instalación.
- Tener Maven instalado o un IDE que venga con su propia instalación.
- Tener acceso a Internet para poder clonar el repositorio.

El IDE recomendado para ejecutar el programa es Eclipse, porque es el que usamos para desarrollar el proyecto.

### Desde el terminal

Abre un terminal en cualquier carpeta y sigue estos pasos:

1. Clona el repositorio: `git clone https://github.com/cesar-gp/biblioteca.git`.
2. Abre en la carpeta que lo contiene: `cd biblioteca`.
3. Compila el proyecto con Maven: `mvn package`.
4. Ejecútalo con Java: `java -cp target/biblioteca-0.0.1-SNAPSHOT.jar dam.biblioteca.Main`.

### Desde Eclipse

1. Abre la carpeta que contiene tu *workspace* de Eclipse.
2. Clona el repositorio: `git clone https://github.com/cesar-gp/biblioteca.git`.
3. Cuando abras Eclipse, el proyecto debería estar en la lista de la izquierda. Haz click derecho en su nombre.
4. Despliega el submenú *Run As* y haz click en *Maven install*.
5. Tras hacer esto, podrás pulsar sin problemas el botón de ejecución de la barra superior.

### Desde otro IDE

Los pasos mencionados para Eclipse deberían funcionar en cualquier otro IDE, con una mínima adaptación. Sin embargo, no hemos probado a ejecutarlo en ningún otro IDE y no podemos ofrecer ninguna garantía al respecto.

## Autores
- [César Gutiérrez Pérez](https://github.com/cesar-gp)
- [Rubén Benítez Soler](https://github.com/rbenitezsoler-cmd)