# POC-Spring Data Query By Example (QBE)

## ¿Qué es Query by Example?

Query By Example (a partir de ahora QBE) es una funcionalidad que implementa Spring mediante su librería de Spring Data, la cuál nos permite realizar consultas a base de datos de manera filtrada a partir de un objeto de entrada, que debe ser idéntico en sus propiedades a la propia entidad del modelo datos que se emplea para dicha consulta. 

Una vez se llama a la base de datos usando QBE con este objeto del modelo de datos como "parámetro de entrada", el resultado de la llamada será un listado de objetos los cuáles coincidirán en sus propiedades con las propiedades del objeto de entrada que se ha pasado al realizar dicha consulta. Un ejemplo sería la siguiente imagen:

![QBE Example](/images/QBE.jpg)

En este enlace podemos encontrar la documentación oficial que Spring nos proporciona sobre esta funcionalidad: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#query-by-example

### Limitaciones de QBE a tener en cuenta

Como bien nos indica la documentación oficial, QBE consta de ciertas limitaciones a la hora de realizar el filtrado de datos. Dichas limitaciones se han de tener en cuenta y serían las siguientes
- No se admiten restricciones de propiedades anidadas o agrupadas, como **firstname = ?0 or (firstname = ?1 and lastname = ?2).= ?1 and lastname = ?2)**.
- Solo admite la coincidencia de inicios/contenidos/finales/regex para cadenas y la coincidencia exacta para otros tipos de propiedades.

De todas maneras, en este documento vamos a tratar de dar solución a estas limitaciones que QBE nos plantea. En siguientes pasos veremos como para casos en los que se nos requiera usar una consulta por un rango numérico, podremos hacer uso de QBE si, además, mezclamos esta funcionalidad con el sistema de **Specifications** que también implementa Spring Data.

## Instrucciones de ejecución de esta prueba de concepto:

- En la carpeta src/main/resources/docker_postgres_with_data se incluye un fichero docker-compose el cuál hay que ejecutar para que se genere un contenedor que implementa la BBDD de la cuál vamos a hacer uso en este documento. 

    Con docker instalado, ejecutar en una terminal **docker-compose up -d**.

    Para conectar vía Dbeaver o similar:
        Database: postgres
        Puerto: 5438
        Usuario: postgres
        Contraseña: postgres
- Una vez ejecutado lo anterior ya tendremos la base de datos levantada a nivel de contenedor local y podremos ejecutar el código del proyecto que se incluye en este repositorio
- Los endpoints a los que se invoca se pueden consultar en el controller del propio código.

## Imaplantación de QBE

Todo lo que se detalle a continuación se puede encontrar de manera práctica ya realizado en el código de este propio repositorio con lo que, **no entramos a detallar todas y cada una de la clases que componen este proyecto.** Se hará un breve detalle de una caso práctico en concreto.

1. Antes de nada, cabe apuntar que para esta prueba de concepto hemos hecho uso de una base de datos de ejemplo que está publicada en **https://github.com/jdaarevalo/docker_postgres_with_data**. Utilizaremos esta base de datos ya que es de libre uso y, además, nos proporciona la posibilidad de utilizar un modelo de datos relacional el cuál puede sernos de más ayuda aún para entender casos en los que las consultas puedan complicarse un poco.

    **Esta base de datos se incluye en la propia prueba de concepto en formato docker-compose en la carpeta /resources.**

    El diagrama de la base de datos que vamos a utilizar sería el siguiente:

    ![Diagram database](/images/ER_Diagram.png)


2. Una vez aclarado el punto de la base de datos que implementaremos en esta prueba de concepto, tendremos que asegurarnos de estar utilizando la dependencia de Spring Data en nuestro proyecto:

    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    ```

3. Declararemos un clase de entidad para la tabla **Sale** de la base de datos que mostramos en el punto 1. **Cabe apuntar que en dicha clase ya están mapeados los atributos relaciones que hacen referencia a las otras tablas de la base de datos**.

    ```
    @Data
    @Entity
    public class Sale implements Serializable{
        private static final long serialVersionUID = -335857734346264194L;
        
        @Id	
        private String saleId;
        
        private BigDecimal amount;
        
        private LocalDateTime dateSale;
        
        @ManyToOne()
        @JoinColumn(name = "product_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Product product;
        
        @ManyToOne()
        @JoinColumn(name = "store_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Store store;
        
        @ManyToOne()
        @JoinColumn(name = "user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private User user;
        
        @JsonManagedReference
        @OneToMany(mappedBy = "sale")
        private List<OrderStatus> orderStatus;
    }
    ```

4. Declarar una interfaz que extienda de JpaRepository

    ```
    public interface SaleRepository extends JpaRepository<Sale, Long>, JpaSpecificationExecutor<Sale>{

    }
    ```

## Uso de QBE

A continuación se detallan diferentes casos de uso que se podrían dar en un escenario real en el cuál usaremos QBE.

1. **Se obtiene un listado en el cual las propiedades sean 100% coincidentes con el objeto que informamos como entrada a la consulta**: 

    ```
    @PostMapping("/searchFullObject")
	public ResponseEntity<List<Sale>> getAllSalesSearch(@RequestBody Sale sale){
		
		List<Sale> sales = saleRepository.findAll(Example.of(sale));
		
		return !sales.isEmpty() 
				? new ResponseEntity<>(sales, HttpStatus.OK) 
						: new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
    ```
    Como podemos observar en el ćodigo, hacemos uso de **Example.of(sale)** como parámetro de entrada en el método findAll que nos proporciona Spring Data. De esta manera tan sencilla ya estaremos realizando un filtrado de datos a partir de un objeto de entrada.

    **Importante: Las propiedades que estén con el valor null en el objeto de entrada QBE no las toma en cuenta**

2. **Se obtiene un un listado en el cual la propiedad product.name CONTIENE la cadena que tiene la propiedad sale.product.name informada en el objeto de entrada. Además, si el resto de los campos vienen informados, también se tienen en cuenta de manera que deben coincidir con las propiedades informadas en el objeto de entrada.**

    ```
	@PostMapping("/searchWithProductNameContain")
	public ResponseEntity<List<Sale>> getAllSalesSearchWithProductNameContains(@RequestBody Sale sale){
		
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("product.name", match -> match.contains());
				// .withMatcher("product.name", match -> match.endsWith())
				// .withMatcher("product.name", match -> match.startsWith())...;

		List<Sale> sales = saleRepository.findAll(Example.of(sale, matcher));		
		
		return !sales.isEmpty() 
				? new ResponseEntity<>(sales, HttpStatus.OK) 
						: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    ```

    Como podemos observar, para este tipo de casos y otros como los que se han dejado comentados en el propio código, QBE implementa el uso de los ExampleMatcher. Esto nos permite decirle a la consulta que para X campo, se le apliquen unas condiciones de búsqueda como pueden ser contains, startsWith, endWith, ignoreCase (mayusculas o minusculas), exact... y otras más. Estas condiciones podemos encontrarlas en la propia docuemtación de spring data.

3. **Filtrado por rangos**

    Este caso es algo más complejo ya que conlleva mezclar QBE con **Especificaciones** para resolver la restricción que QBE tiene a la hora de realizar búsquedas por rangos numéricos. Por ejemplo, en este caso de uso, se realiza la implementación de una Specification que, además de recibir el QBE que hemos definido, también realiza un filtrado para identificar aquellos casos en los que **amount > 3000**.

    Para ello, en la entidad del modelo de datos que hemos definido, hemos inluido la **Specification** en concreto que suplirá la carencia que QBE tiene en estos casos. La entidad del modelo de datos, una vez hemos incluido la Specification quedaría de la siguiente manera:

    Las **Especificaciones de SpringData**, además de para este caso se pueden emplear para otros casos de uso los cuáles no podamos solventar vía QBE de manera independiente, como puede ser, por ejemplo, las consulta anidadas. 
    No entraremos en esta prueba de concepto en mostrar y explicar detalladamente el uso de especificaciones ya que nos alargaríamos demasiado. Para leer más acerca de esto, en enlace a la documentación oficial es el siguiente: **https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications**

    ```
    @Data
    @Entity
    public class Sale implements Serializable{
        private static final long serialVersionUID = -335857734346264194L;
        
        @Id	
        private String saleId;
        
        private BigDecimal amount;
        
        private LocalDateTime dateSale;
        
        @ManyToOne()
        @JoinColumn(name = "product_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Product product;
        
        @ManyToOne()
        @JoinColumn(name = "store_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Store store;
        
        @ManyToOne()
        @JoinColumn(name = "user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        private User user;
        
        @JsonManagedReference
        @OneToMany(mappedBy = "sale")
        private List<OrderStatus> orderStatus;
        
        public static Specification<Sale> buildPredicate(Example<Sale> example){
            return (root, query, builder) -> {
                List<Predicate> predicateList  = new ArrayList<>();
                predicateList.add(builder.and(QueryByExamplePredicateBuilder.getPredicate(root, builder, example)));
                predicateList.add(builder.greaterThan(root.get("amount"), new BigDecimal("3000")));
                return builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            };
        }        
    }
    ```

    Una vez aquí, realizaríamos la llamada al siguiente enpoint que ejecutaría lo que se muestra a continuación:

    ```
    @PostMapping("/searchWithQBEAndSpecification")
	public ResponseEntity<List<Sale>> getAllSalesSearchWithProductNameContainsAndAmountGreater(@RequestBody Sale sale){
		
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("product.name", match -> match.contains());

		List<Sale> sales = saleRepository.findAll(Sale.buildPredicate(Example.of(sale, matcher)));		
		
		return !sales.isEmpty() 
				? new ResponseEntity<>(sales, HttpStatus.OK) 
						: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	} 
    ```

    De esta manera, estamos mostrando una manera de fusionar los QBE con las Especificaciones lo cuál nos "mitiga" las carencias que tiene QBE a la hora de realizar filtrado de datos más profundos y consultas anidadas.     
