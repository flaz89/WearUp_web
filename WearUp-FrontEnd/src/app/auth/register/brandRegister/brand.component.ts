import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ViewChild, ElementRef } from '@angular/core';
import { imageFileValidator } from '../uploadValidator';
import { AuthService } from 'src/app/services/auth.service';
import { BrandRegister } from 'src/app/model/BrandRegister.interface';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.scss']
})
export class BrandComponent implements OnInit {

  imageSrc: string = '';
  isImageLoaded:boolean = false;
  @ViewChild('fileInput') fileInput!: ElementRef;

  @Output() registrationSuccess = new EventEmitter<void>();

  imageForm!: FormGroup;
  logoForm!: FormGroup;
  detailsForm!: FormGroup;
  selectedBrandImage: File | null = null;
  selectedBrandLogo: File | null = null;

  isLoading:boolean = false;
  serverMessageOk!:string;
  serverMessageError!:string;

  countries: any[] = [];
  selectedCountry: any;



  constructor(private fb: FormBuilder, private authSrv:AuthService, private router: Router, private http:HttpClient) { }

  ngOnInit(): void {
    this.imageForm = this.fb.group({
      profilePicture: ['' || null,[imageFileValidator]]
    });

    this.logoForm = this.fb.group({
      brandLogo: ['' || null,[imageFileValidator]]
    });



    this.detailsForm = this.fb.group({
      brandName: ['', Validators.required],
      webSite: [null],
      country: ['', Validators.required],
      city: ['', Validators.required],
      address: ['', Validators.required],
      phoneNumber: [null],
      vatNumber: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });


    this.http.get("https://countriesnow.space/api/v0.1/countries/states")
      .subscribe(
        (data: any) => {
          this.countries = data.data || [];
        },
        error => {
          console.error("Error fetching countries:", error);
        }
      );
  }

  //-------------- GESTIONE IMMAGINI
  imageUploaded(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedBrandImage = file;
      console.log(file);


      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imageSrc = reader.result as string;
        this.isImageLoaded = true;
      };
    }
  }

  logoUploaded(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedBrandLogo = file;

      const reader = new FileReader();
      reader.readAsDataURL(file);
    }
  }

  cancelImage() {
    this.fileInput.nativeElement.value = '';
    this.imageForm.get("profilePicture")?.reset;
    this.isImageLoaded = false;
    this.imageForm.reset();
  }


  //-------------- SUBMIT FORM

  onSubmit() {
    this.isLoading = true;


    if (this.logoForm.valid && this.detailsForm.valid) {

    if (this.selectedBrandLogo) {
      const uploads: Observable<any>[] = [];

      // Aggiungi l'upload per il brandLogo, che è obbligatorio
      const formDataLogo = new FormData();
      formDataLogo.append('file', this.selectedBrandLogo);
      console.log(formDataLogo);

      uploads.push(this.authSrv.uploadBrandImage(formDataLogo));

      // Se è presente profilePictureValue, aggiungi anche questo upload
      if (this.selectedBrandImage) {
        const formDataImage = new FormData();
        formDataImage.append('file', this.selectedBrandImage);
        console.log(formDataImage);

        uploads.push(this.authSrv.uploadBrandImage(formDataImage));
      }

      forkJoin(uploads).subscribe(responses => {
        const brandRegisterData: BrandRegister = {
          ...this.detailsForm.value,
          brandLogo: responses[0].url, // brandLogo è sempre il primo
          profilePicture: this.selectedBrandImage ? responses[1]?.url : null,
        };
        this.authSrv.resgisterBrand(brandRegisterData).subscribe(
          response => {
            this.isLoading = false;
            this.serverMessageOk = "Brand successfully registered"
            console.log('Brand registrato con successo:', response);
            setTimeout(() => {
                this.registrationSuccess.emit();
              }, 1500);
          },
          error => {
            console.error('Error during registration:', error);
            this.serverMessageError = "Error during file saving";
            this.isLoading = false;
          }
        );
        }, error => {
          this.isLoading = false;
          this.serverMessageError = "Error uploading images";
          console.log(error);
        });
      } else {
        // Gestisci il caso in cui brandLogo non è presente
        this.isLoading = false;
        this.serverMessageError = "Brand logo is required";
      }
      } else {
        // Gestisci il caso in cui i form non sono validi
        this.isLoading = false;
        this.serverMessageError = "Invalid form data";
      }
  }


  // onSubmit() {
  //   this.isLoading = true;
  //   // console.log(this.detailsForm.value);
  //   // console.log('brandName is valid:', this.detailsForm.get('brandName')?.valid);
  //   // console.log('webSite is valid:', this.detailsForm.get('webSite')?.valid);
  //   // console.log('country is valid:', this.detailsForm.get('country')?.valid);
  //   // console.log('city is valid:', this.detailsForm.get('city')?.valid);
  //   // console.log('address is valid:', this.detailsForm.get('address')?.valid);
  //   // console.log('phoneNumber is valid:', this.detailsForm.get('phoneNumber')?.valid);
  //   // console.log('vatNumber is valid:', this.detailsForm.get('vatNumber')?.valid);
  //   // console.log('email is valid:', this.detailsForm.get('email')?.valid);
  //   // console.log('password is valid:', this.detailsForm.get('password')?.valid);




  //   if (this.imageForm.valid && this.detailsForm.valid) {
  //     const profilePictureValue = this.imageForm.get('profilePicture')?.value;
  //     if (profilePictureValue) {
  //       const formDataImage = new FormData();
  //       formDataImage.append('file', profilePictureValue);

  //       this.authSrv.uploadBrandImage(formDataImage).subscribe((response: any) => {
  //         // Ottieni l'URL dell'immagine caricata dal server
  //         const uploadedImageUrl = response.url;
  //         // Assegna l'URL all'oggetto UserRegister
  //         const brandRegisterData: BrandRegister = {
  //           ...this.detailsForm.value,
  //           profilePicture: uploadedImageUrl
  //         };
  //         // Ora puoi effettuare la chiamata POST per registrare l'utente
  //         this.authSrv.resgisterBrand(brandRegisterData).subscribe(response => {
  //           this.isLoading = false;
  //           this.serverMessageOk = "Brand saved successfully";
  //           console.log(response);
  //           setTimeout(() => {
  //             this.registrationSuccess.emit();
  //           }, 1500);

  //         }, error => {
  //           this.isLoading = false;
  //           this.serverMessageError = error.error.message;
  //           console.log(response.error.message);
  //         });

  //       }, error => {
  //         this.isLoading = false;
  //         this.serverMessageError = "Error loading image";
  //           console.log(error);
  //       });
  //     } else {
  //       // Caso in cui non viene caricata un'immagine
  //       const brandRegisterData: BrandRegister = {
  //         ...this.detailsForm.value
  //       };
  //       // Effettua la chiamata POST come al solito
  //       this.authSrv.resgisterBrand(brandRegisterData).subscribe(response => {
  //         this.isLoading = false;
  //         this.serverMessageOk = "Brand saved successfully";
  //           console.log(response);
  //           setTimeout(() => {
  //             this.registrationSuccess.emit();
  //           }, 1500);
  //       }, error => {
  //         this.isLoading = false;
  //         this.serverMessageError = error.error.message;
  //         console.log(error.error.message);
  //       });
  //     }
  //   }
  // }

  ////-------------- ESTRAGGO e ORGANIZZO LE CITTA'
  onCountryChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedCountryIso = selectElement.value;
    this.selectedCountry = this.countries.find(country => country.iso2 === selectedCountryIso);

    if (this.selectedCountry && this.selectedCountry.states) {
      this.selectedCountry.states.sort((a:any, b:any) => {
        if (a.state_code < b.state_code) {
          return -1;
        }
        if (a.state_code > b.state_code) {
          return 1;
        }
        return 0;
      });
    }
  }


}




