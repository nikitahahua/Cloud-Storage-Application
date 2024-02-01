# Cloud Storage Application 
 
---

This project built using the following technologies: 
* Spring Boot
* Spring Data
* Spring Security
* Spring Sessions, Redis
* Hibernate, MySQL, Flyway
* Lombok
* RabbitMQ
* Docker
* MinIO

---

## API Documentation

### description :

The main functionality includes user registration and authorization, the ability to upload and manage files and folders, create new folders, delete and rename, which makes it a convenient tool for organizing data. The application interface includes a main page with a search form and a breadcrumb for easy navigation, as well as a file search page with the ability to navigate to the folders containing them.

---

###### Register:
POST /auth/register - Register a new user with a username, email, and password.

```json
{
    "username": "some-name",
    "email": "somemeail@gmail.com",
    "password": "some-password"
}
```

## File functions:

you have to log in by adress http://localhost:9001/login

credentials:
* Username: minioadmin
* Password: minioadmin
  
![Снимок экрана 2024-02-01 в 01 59 12](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/b503c146-c212-45d8-b4a0-185b84e02216)

there will be single backet "user-files" for this project 

when you register application will create root folder named like user-${user's id in db}-files, so you'll see something like this: 

also you can see other users folders but you have bo access to them
![Снимок экрана 2024-02-01 в 02 02 17](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/78bc08c4-31a5-4e62-89d7-16ada323b2c4)

---

### You can delete some file or folder from your root package, example:

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/56810ccc-3b2f-4c81-938f-23fdd216986d)

<img width="721" alt="image" src="https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/67afa120-a65b-41b0-872d-be5351a8cea9">

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/236b3634-cbea-40b1-8e9d-debff3e5ed69)

result:

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/a1ee6532-a2e4-42a7-87f6-d63cd7267f80)

### Let's try to download some file, on example i do it via browser:

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/49d71ab5-5b88-42ae-b7b5-cd0f0f3157cd)

---

#### http://localhost:8080/download-file?path=user-42-files/4d3a97b20a72f741931c98447ef0a5ac.jpg

---

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/99d7cb1f-9802-475e-b5e2-e6b98c51c78e)

#### Successfully downloaded!!!

---

## Let's upload some file:

<img width="648" alt="image" src="https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/c859542b-609c-4300-9fd4-89cafdaa5d99">

## Result:

![Снимок экрана 2024-02-01 в 02 59 56 1](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/26628ad8-5ba3-49d8-9d96-03629b5e676c)

---

## Let's Rename some folder:

![Снимок экрана 2024-02-01 в 03 04 30](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/a9fbb68d-cb3b-4285-9379-e3157913f028)

there is files inside;

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/6d6648e6-b526-4382-b506-930fbd9bdb85)

<img width="674" alt="image" src="https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/e374e90b-b99d-49b8-b0a3-6b9d19c2b06e">

## Result:

![Снимок экрана 2024-02-01 в 03 06 12](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/a0f1978f-07ee-422b-a93b-0ec73684fbcd)

![image](https://github.com/nikitagagua/Cloud-Storage-Application/assets/110698480/ae7371a5-bd43-4972-86e4-497eb0ad7f1b)

---

You can import the provided Postman collection file (CloudStorageRequests.postman_collection.json) to easily test the API endpoints. The collection includes sample requests for various API actions.







