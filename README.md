# TeamApp

Este proyecto consiste en una aplicación fullstack compuesta por:

- Un frontend desarrollado con Vue 3
- Varios módulos backend desarrollados con Spring Boot

La aplicación proporciona funcionalidad para que equipos deportivos amateurs puedan gestionar las necesidades diarias de forma sencilla y centralizada.

## Estructura del proyecto

La estructura del repositorio es la siguiente:

/
├── teamapp-db/         # Carpeta que contiene los scripts de base de datos
├── teamapp-persist/    # Paquete de persistencia de datos (Spring Boot)
├── teamapp-server/     # Paquete principal del backend que contiene el server (Spring Boot)
├── teamapp-service/    # Paquete de implementación de lógica de negocio
├── teamapp-web/        # Paquete que contiene la aplicación cliente/frontal (Vue 3)
└── README.md           # Este archivo

## Requisitos previos

- Node.js (versión 22)
- Java JDK 24 o superior
- Maven 3.9.9
- Git

## Instalación y ejecución

### Backend

Para compilar el server es necesario ejecutar el siguiente comando maven:

1. Desde la raiz del proyecto ejecutar
   ./mvn clean install

Para levantar el server de backend con maven:

1. Desde la raiz del proyecto ejecutar
   ./mvnw spring-boot:run


### Frontend

1. Entrar en la carpeta del frontend:
   cd teamapp-web/src/main/frontend

2. Instalar dependencias:
   npm install

3. Iniciar el servidor de desarrollo:
   npm run dev

La aplicación estará disponible en http://localhost:5173 (por defecto).


## Configuración

Los archivos de configuración para el backend están situados en el paquete teamapp-server. En la ruta:

/teamapp-server/src/main/resources

Se encuentra el `application.properties` principal y para cada entorno de desarrollo.

Los archivos de configuración para el frontend están situados en el paquete teamapp-web. En la ruta:

/teamapp-web/src/main/frontend

Se encuentra el `.env` principal y el de cada entorno de desarrollo.

## Notas

- Es recomendable ejecutar primero el backend antes de iniciar el frontend.
- Los puertos por defecto pueden modificarse en los archivos de configuración correspondientes.
