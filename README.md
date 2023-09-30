<img src="https://res.cloudinary.com/wearup/image/upload/v1696033758/WearUp/images/banner_read-me_Artboard_19_ovzv7w.png" width="100%">

# WearUp - Fashioning the Future
WearUp is a project initially conceived for mobile apps (iOS/Android), but transformed into a web-app for this initial phase of its life.
It wants revolutionize and innovate what is currently the classic experience that brands offer to the customer during the purchase phase through various e-commerce and various smartphone apps. What WearUp wants to offer is a "purchasing awareness" experience, combining fun and seriousness, exploiting the most cutting-edge technologies such as 3D experience and Augmented Reality for the virtual try-on.

Questa demo offre all'utente di sfruttare il dettaglio che solo la tecnologia 3D è in grado di ofrrire.
L'utente può "giocare" con alcuni modelli di prova senza registrarsi per testare il prodotto, oppure accedere per avere l'elenco completo dei prodotti a disposizione. L'accesso può avvenire come "user", ovvero utente semplice con account e gestione dei preferiti, oppure come "brand", dedicato a tutti i marchi di moda innovativi che decidono di sfruttare la potenzialità di questa app caricando i propri prodotti digitali mettendoli a disposizione del grande pubblico.

CHOOSE BETTER.  
BE AWARE.  
WEAR IT UP.

## Index

- [Front-end](#front-end)
- [Back-end](#back-end)
- [Assets](#back-end)
- [Installation](#installazione)
- [Author](#utilizzo)


## <img src="WearUp-FrontEnd/src/assets/logos/Loghi_Brand_Wap_Read-me-03.png" width="40" height="30"> Front-end
The Front-end was developed using the following technologies:

### 💻 Technologies

- Angular CLI: 14.2.12 <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-meangular_pkkoyc.png" height="20">
- Node: 18.15.0 <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-menode_prfp3b.png" height="20">
- Package Manager: npm 9.5.0 <img src="https://res.cloudinary.com/wearup/image/upload/v1696028247/WearUp/images/logo_read-menpm_tyoggc.png" height="20"> 
- Bootstrap 5 <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-mebootstrap_zqac8s.png" height="20">
- Three.JS <img src="https://res.cloudinary.com/wearup/image/upload/v1696028247/WearUp/images/logo_read-methree.js_xeb89y.png" height="20">

### ⚙️ Functionality

- Landing on the "Home" page
- Discover 3D by accessing the "Try it" page without registration, where you can see the potential of WearUp by playing with the 4 most liked products
- Register as a "User" or a "Brand"
- USER: research of the product / like product / interact with product in a 3D environment / get product specifications / get product link
- BRAND: research of the product / upload product / interact with product in a 3D environment / get product specifications / get product link
- Page "products" allows user to interact with all products catalog and 3D viewer (only with registration)

## <img src="WearUp-FrontEnd/src/assets/logos/Loghi_Brand_Wap_Read-me-03.png" width="40" height="30"> Back-end
The Front-end was developed using the following technologies:

### 💻 Technologies

- Java JDK: 17.0.7 <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-me_jdk_oljgmq.png" height="20">
- Springboot <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-me_springboot_gwhcf4.png" height="20">
- PosgreSQL <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-me_postgresql_e5uxqz.png" height="20"> 
- Cloudinary <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-me_cloudinary_hyak0g.png" height="20">
- JWT <img src="https://res.cloudinary.com/wearup/image/upload/v1696028246/WearUp/images/logo_read-me_jwt_vjmamg.png" height="20">
- Bcrypt <img src="https://bcrypt.online/images/bcrypt-esse-tools-logo-square.svg" height="20">

### ⚙️ Functionality

- PostgreSQL used as database application, all the database data access are stored in a private file so if you want to use it you have to link your database data on file "env.properties"
- All digital assets managed using Cloudinary as a cloud storage
- JWT used to generate token for each user access
- Bcrypt used for cryprting all users password
## <img src="WearUp-FrontEnd/src/assets/logos/Loghi_Brand_Wap_Read-me-03.png" width="40" height="30"> Assets

### 💻 Technologies

- Figma <img src="https://res.cloudinary.com/wearup/image/upload/v1696029535/WearUp/images/logo_read-me_figma_kep5hz.png" height="20">
- Illustrator <img src="https://res.cloudinary.com/wearup/image/upload/v1696029532/WearUp/images/logo_read-me_ai_fxb83h.png" height="20">
- Clo3D <img src="https://res.cloudinary.com/wearup/image/upload/v1696029533/WearUp/images/logo_read-me_clo_na4boe.png" height="20">

### ⚙️ Functionality

- Figma used for all the UI Design
- Illustrator used for the UI creation from scratch of custom logos vector images
- Clo3D used for the design and the develop of all virtual assets

## <img src="WearUp-FrontEnd/src/assets/logos/Loghi_Brand_Wap_Read-me-03.png" width="40" height="30"> Installation

To run the project you need use:
- FRONT-END: run CLI command `ng serve`
- BACKEND: run application normally, but before you have to change data on "env.properties" file because are private data

## 🖊️ Author
<a href="https://www.linkedin.com/in/flavio-mammoliti-52138167/">Flavio Mammoliti</a>
