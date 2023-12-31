import { AbstractControl, ValidationErrors } from '@angular/forms';


export function imageFileValidator(control: AbstractControl): ValidationErrors | null {
  const file = control.value;

  if (file) {
    const extension = file.split('.').pop().toLowerCase();
    if (['jpg', 'jpeg', 'png'].indexOf(extension) < 0) {
      return { 'invalidExtension': true };
    }
  }
  return null;
}

export function fbxFileValidator(control: AbstractControl): ValidationErrors | null {
  const file = control.value;

  if (file) {
    const extension = file.split('.').pop().toLowerCase();
    if (extension !== 'fbx') {
      return { 'invalidExtension': true };
    }
  }
  return null;
}
