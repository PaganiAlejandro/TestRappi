#   TEST RAPPI 

## Las capas de la aplicación y qué clases pertenecen a cual.
#####  Capa de modelo
Las clases dentro de esta capa son:
Movie, MoviePageResult,MovieReview,MovieReviewPageresult,MovieTrailer, MovieTrailerPageResult

##### Capa de Red
Las clases dentro de esta capa son:
GetMovieDataService, GetMovieReviewService, GetMovieTrailerService y RetrofitInstance.

##### Capa de Vista
Las clases dentro de esta capa son:
MainActivity, MovieActivity, ReviewActivity, MovieAdapter,ReviewAdapter,TrailerAdapter, MovieViewHolder,ReviewViewHolder,TrailerViewHolder

## Responsabilidad de cada clase creada.

##### Package network:
La comunicación con la Api se llevó a cabo por medio de la libreria Retrofit. Retrofit es un cliente HTTP de tipo seguro para Android y Java, que  hace sencillo conectar a un servicio web REST traduciendo la API a interfaces Java. Hace sencillo consumir datos JSON, que después son parseados en Objetos Java (Plain Old Java Objects, POJOs).
Retrofit no tiene un convertidor JSON integrado para convertir de objetos JSON a Java. En su lugar, viene con soporte para librerías de convertidor JSON, como puede ser Gson, que es la que se utilizó en este caso.
Las interfaces GetMovieDataService, GetMovieReviewService y GetMovieTrailerService son las encargadas de exponer el llamado a los métodos Get de la API.
La clase RetrofitInstance es la encargada de generar la instancia de Retrofit, debido que  para emitir peticiones de red a una API RESTFUL con Retrofit, necesitamos crear una instancia usando la clase Retrofit Builder y configurarla con una URL base. 

##### Package model:
Cada una de las clases contenidas en este package son Pojos que mapean directamente con los datos que la Api nos provee en formato Json y que con la ayuda de Gson son transformados a objetos.

##### Package ui:
El contenido de este paquete, son todas clases e interfaces que ayudan a darle el comportamiento a la vista.

**MainActivity**: Es la clase/actividad principal. La misma es la encargada de mostrar las listas de películas seleccionadas, de filtrar las películas, de realizar las peticiones a la Api, de cachear los datos.

**MovieActivity**: Actividad encargada de mostrar los detalles de una película seleccionada (Poster, Titulo, Detalle, Fecha de lanzamiento, Clasificación, Opiniones y Trailers).

**ReviewActivity**: Actividad encargada de mostrar las diferentes opiniones.

En cada una de las actividades se hizo uso de ReciclerView, para crear un adaptador para cada RecyclerView, se debe extender de RecyclerView.Adapter. Éste adaptador sigue el patrón de diseño view holder, que significa que define una clase interna que extienda de RecyclerView.ViewHolder. Éste patrón minimiza el número de llamadas al costoso método findViewById. 
Por este motivo tenemos 3 clases Adapter que extienden de RecyclerView.Adapter (**MovieAdapter, ReviewAdapter y TrailerAdapter**)
Y 3 clases que extienden de RecyclerView.ViewHolder  (**MoviewViewHolder, ReviewViewHolder y TrailerViewHolder**).

**MovieClickListener** es una interfaz para la escucha de los click. Con esta interfaz puedo configurar una clase de titular de vista como un detector de clics y pasar una instancia de mi interfaz. Luego configuro la vista como un detector de clics y llamo a mi interfaz con la posición apropiada.

**TrailerClickListener** la funcion de esta interfaz, es la misma que la anterior.

**EndlessRecyclerViewScrollListener** en la clase que contiene el mecanismo para el paginado  que permite ir obteniendo desde la API, los diferentes elementos a mostrar a medida que el usuario vaya solicitándolos al hacer scroll en la lista principal de películas.

## En qué consiste el principio de responsabilidad única? Cuál es su propósito?

El principio de responsabilidad única consiste en mantener una única responsabilidad en un módulo, o que es lo mismo debe de tener una única razón para cambiar. Mas de una responsabilidad hace que el código sea difícil de leer, de testear y mantener. Es decir, hace que el código sea menos flexible, mas rígido.

Este principio es el primero del acrónimo SOLID, para la programación orientada a objetos.

Por desgracia, las necesidades cambian y los usuarios también, es por eso que el primer valor del software es la facilidad al cambio. Es más importante permitir que el software sea lo suficientemente flexible como para poder adaptarse a nuevos usuarios y necesidades que satisfacerlas en un momento concreto. Sin este valor, el secundario es difícil de acometer a largo plazo.

El propósito de este principio es permitir que nuestro código sea mas flexible al cambio y por tanto nos ayuda a mantener el primer valor del software, el de la facilidad al cambio, alto.

[![N|Solid](https://uploads.toptal.io/blog/image/91846/toptal-blog-image-1449597577848-60a7b4874d676e754260b05866cf967f.jpg)](https://nodesource.com/products/nsolid)

## Qué características tiene, según su opinión, un “buen” código o código limpio?

Actualmente en la empresa donde estoy, estamos aplicando esta filosofía, de código limpio y en mi opinión, no es una sola cosa la que debemos tener en cuenta, si no que son varios aspectos. A continuación enumero algunos.
1.	Usa nombres con significado.
El  código tiene que poder leerse como si fuera un libro. Para ello, los nombres de las variables, métodos, clases tiene que seleccionarse con cuidado para que den significado a lo que estamos intentando “contar” en nuestro programa.
2.	Haz unidades de código pequeñas.
Las clases y métodos cortos simplifican la compresión del código y lo hacen mas mantenible.
3.	Las unidades de código deben hacer una única cosa
Muy relacionado al punto anterior y con el principio de responsabilidad única. Centrándonos en una clase por ejemplo, si esta hace varias cosas, tarde o temprano nos encontraremos con que la clase es propensa a cambios, nos cuesta mas testearla, el código crecerá de forma descontrolada y nos será mas dificil agregar nueva funcionalidad.
4.	Las funciones deben tener un número limitado de argumentos
Los parámetros añaden una gran cantidad de carga conceptual que dificulta la lectura. Ademas dificultan el testing.
5.	Sigue el principio DRY: Don’t Repeat Yourself 
Repetir código es una acción bastante peligrosa que nos causará problemas tarde o temprano, y seguir este principio de diseño de software te evitará algunos quebraderos de cabeza.
6.	Evita utilizar comentarios siempre que sea posible
"Un comentario es un síntoma de no haber conseguido escribir un código claro", Robert C.Martin. Si necesitas añadir un comentario, es porque el código no es autoexplicativo, lo que iría en contra del primer punto.
7.	Utilizar un formato único en tu código
Cualquier formato, por extraño que sea, es mejor que ninguno en absoluto. Dará coherencia y estructura al código, y unas pautas a seguir por todos los desarrolladores (Densida vertical, Ubicación de los componentes, Numero de caracteres por línea, etc). 
8.	Abstrae tus datos: no uses getters y setters indiscriminadamente
Una práctica muy común en lenguajes como Java es la de crear un objeto e inmediatamente autogenerar todos sus getters y sus setters para tener acceso y capacidad de modificación de su estado. Aquí es importante identificar si nuestra clase es una simple estructura de datos o es un objeto con comportamiento. Los objetos esconderán sus datos mediante abstracciones y expondrán funciones para operar con ellos.
9.	Ley de Demeter
Nuestro objeto no debería conocer las entrañas de otros objetos con los que interactúa. Es un mecanismo de detección de acoplamiento, y nos viene a decir que nuestro objeto no debería conocer las entrañas de otros objetos con los que interactúa. Si queremos que haga algo, debemos pedírselo directamente en vez de navegar por su estructura.
10.	Establece fronteras
Las fronteras nos permiten establecer límites entre nuestro código y ese código que no controlamos. Existen muchos casos en nuestros software en los que no tenemos control sobre el código que ejecutamos, ya sea por ejemplo cuando utilizamos librerías de terceros o frameworks.
11.	Escribe Tests
No hay mucho que contar aquí. Los tests ayudan a definir y validar la funcionalidad del software que estés implementando.


Todas estas ideas hay que estudiarlas y utilizarlas cuando tenga sentido. Pero si las aplicamos, conseguiremos que nuestro código sea más fácil de leer y de mantener, tendremos un “código limpio”.


## Aclaraciones.
Debido al poco tiempo que dispongo en mis días actualmente, el test no quedo como me hubiera gustado para dar una mejor impresion de lo que uno puede llegar a lograr como desarrollador Android. Me hubiese gustado poder aplicar la "Arquitectura Clean" para enviar una app de calidad. Escribir código de calidad para aplicaciones de complejidad media y alta requiere un esfuerzo y experiencia considerables. Una aplicación no solo debe cumplir con los requisitos del cliente, sino que también debe ser flexible, comprobable y fácil de mantener.

[![N|Solid](https://rubygarage.s3.amazonaws.com/uploads/article_image/file/2060/Artboard_15587.png)](https://nodesource.com/products/nsolid)
