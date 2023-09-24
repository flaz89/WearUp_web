export interface Product {
  productCode: string;
  productName: string;
  description: string;
  type: string;
  price: number;
  link3D: string;
  textures: {
    albedoMap: string;
    alphaMap: string;
    heightMap: string;
    aoMap: string;
    normalMap: string;
    metalnessMap: string;
    roughnessMap: string;
  };
  productPicture: string;
  productLink: string;
}
