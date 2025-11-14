# 🧾 Proyecto Spring Boot — Guía de Desarrollo Local

Este documento describe las tecnologías utilizadas en el proyecto y los pasos necesarios para levantar el entorno de desarrollo local, incluyendo la documentación de la API mediante **Swagger**.

---

## 🚀 Tecnologías utilizadas

### **Backend**
- **Java 24+** (o versión que utilice el proyecto)
- **Spring Boot** (Web, Data JPA, Validation, etc.)
- **Maven** como gestor de dependencias
- **Spring Data JPA** para la capa de persistencia
- **Hibernate** como proveedor JPA
- **MySQL** (dependiendo del proyecto)
- **Swagger / Springdoc OpenAPI** para documentación de APIs REST

### **Herramientas adicionales**
- **IntelliJ IDEA / Eclipse / VS Code** (IDE de desarrollo)
- **Docker** (opcional para levantar base de datos)
- **Git** para control de versiones



---
### **Por defecto el puerto esta en el 8081, se cambia el puerto en el archivo main > resources > application.properties**

http://localhost:8081/swagger-ui/index.html

---

### 1. Clonar el repositorio
```bash
git clone https://github.com/aniicossio1997/TTPS-Java.gi
cd tu-proyecto


