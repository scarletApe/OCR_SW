# OCR_SW
Sopa de Letras con OCR

Este es un proyecto personal que resuelve sopa de letras. Se puede importar una 
imagen estatica que contiene la sopa de letras y si el proceso es exitoso se pueden
buscar las palabras.

El OCR de este proyecto como motor utiliza una red neuronal, la cual fue desarrollada
en el proyecto de tesis, entrendada con letras mayusculas en varias fonts. 

Para buscar las palabras ya en la sopa de letras se utiliza el BOM Backward Matching Algorithm
el cual fue implementado del pseudo codigo de un libro.

## Direcciones de Uso

* Si se tiene una imagen de una sopa de letras, con un programa externo se puede hacer 
cropping para solamente aislar la matriz con las lettras.

![Sopa de Letras Birthday](/images/birthday-word-search.gif)

* Ya teniendo la imagen aislada se puede importar al sistema.

![Importación exitosa](/images/import.png)

* Si la importación fue exitosa, se muestra la matriz en la ventanilla principal donde se puede buscar las palabras.

![Buscar Palabras](/images/sw_resolved.png)
