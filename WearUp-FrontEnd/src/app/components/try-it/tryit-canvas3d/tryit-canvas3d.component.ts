import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { FBXLoader} from 'three/examples/jsm/loaders/FBXLoader.js';

@Component({
  selector: 'app-tryit-canvas3d',
  templateUrl: './tryit-canvas3d.component.html',
  styleUrls: ['./tryit-canvas3d.component.scss']
})
export class TryitCanvas3dComponent implements AfterViewInit {

  @ViewChild('canvasRef') canvasRef!: ElementRef<HTMLCanvasElement>;
  @Input() containerWidth!: number;
  @Input() containerHeight!: number;

  //product
  @Input() productData: any;

  private scene!: THREE.Scene;
  private mesh!: THREE.Object3D | undefined;

  private loadedModel3DLink!: string
  private loadedAlbedoMap!: string;
  private loadedAlphaMap!: string;
  private loadedAoMap!: string;
  private loadedHeightMap!: string;
  private loadedMetalnessMap!: string;
  private loadedNormalMap!: string;
  private loadedRoughnessMap!: string;

  @Output() productBrandLogo = new EventEmitter<number>();

  constructor() { }

  // ngOnInit(): void {
  //   console.log('OnInit:', this.productData.link3D);


  // }

  ngAfterViewInit(): void {
    this.loadedModel3DLink = this.productData.link3D;

    THREE.ColorManagement.enabled = false;
    const canvas = this.canvasRef.nativeElement;

    //* SCENE
    this.scene = new THREE.Scene();


    // LOAD MESH
    // if (this.loadedModel3DLink) {
    //   this.loadModel();
    // }

    //* SIZES canvas
     const sizes = {
       width: canvas.offsetWidth,
       height: canvas.offsetHeight
     };

     window.addEventListener("resize", () => {
      sizes.width = this.containerWidth;
      sizes.height = this.containerHeight;

      camera.aspect = sizes.width / sizes.height;
      camera.updateProjectionMatrix();
      renderer.setSize(sizes.width, sizes.height)

      controls.update();
      renderer.setPixelRatio(Math.min(devicePixelRatio, 2));
    });

    //* CAMERA
    const camera = new THREE.PerspectiveCamera(75, sizes.width/sizes.height);
    this.scene.add(camera);
    camera.position.set(0,0.5,3)

    //* LIGHT
    const directionalLight = new THREE.DirectionalLight(0xffffff, 3);
    const ambientlight = new THREE.AmbientLight(0xffffff, 2)
    directionalLight.position.set(0,5,4)
    this.scene.add(directionalLight, ambientlight);



    // CONTROLS
    const controls = new OrbitControls(camera, this.canvasRef.nativeElement);
    controls.enableDamping = true;
    controls.minDistance = 1.3;
    controls.maxDistance = 3.5;

    //* RENDERER
    const renderer = new THREE.WebGLRenderer({
      canvas: this.canvasRef.nativeElement,
      alpha: true
    })
    renderer.outputColorSpace = THREE.LinearSRGBColorSpace;
    renderer.setSize(sizes.width, sizes.height);

    //clock
    const clock = new THREE.Clock()

    //* ANIMATIONS
    const frameLoop = ()=> {

      // secondi
      const elapsedTime = clock.getElapsedTime();

      if(this.mesh){
        this.mesh.rotation.y = elapsedTime * 0.06
      }

      controls.update();
      renderer.render(this.scene, camera)
      window.requestAnimationFrame(frameLoop)
    }

    frameLoop();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['productData'] && changes['productData'].currentValue) {
      this.loadedModel3DLink = this.productData.link3D;
      this.loadModel();
    }
  }

  loadModel():void{

    if (!this.loadedModel3DLink) {
      return;
    }

    if(this.productData.brand.brandLogo) {
      this.productBrandLogo = this.productData.brand.brandLogo;
      //console.log(this.productBrandLogo);

    }

    if (this.mesh) {
      this.scene.remove(this.mesh);
      this.mesh = undefined;

    }

    const fbxLoader = new FBXLoader();

      fbxLoader.load(this.loadedModel3DLink, fbx => {
        fbx.traverse(child => {
          if (child instanceof THREE.Mesh) {
           //const material = new THREE.MeshStandardMaterial();

           const albedoMap = this.productData.textures.albedoMap ? new THREE.TextureLoader().load(this.productData.textures.albedoMap) : undefined;
           const alphaMap = this.productData.textures.alphaMap ? new THREE.TextureLoader().load(this.productData.textures.alphaMap) : undefined;
           const aoMap = this.productData.textures.aoMap ? new THREE.TextureLoader().load(this.productData.textures.aoMap) : undefined;
           const displacementMap = this.productData.textures.displacementMap ? new THREE.TextureLoader().load(this.productData.textures.displacementMap) : undefined;
           const metalnessMap = this.productData.textures.metalnessMap ? new THREE.TextureLoader().load(this.productData.textures.metalnessMap) : undefined;
           const normalMap = this.productData.textures.normalMap ? new THREE.TextureLoader().load(this.productData.textures.normalMap) : undefined;
           const roughnessMap = this.productData.textures.roughnessMap ? new THREE.TextureLoader().load(this.productData.textures.roughnessMap) : undefined;

            const material = new THREE.MeshStandardMaterial({
              map: albedoMap,
              alphaMap: alphaMap,
              aoMap: aoMap,
              displacementMap: displacementMap,
              metalnessMap: metalnessMap,
              normalMap: normalMap,
              roughnessMap: roughnessMap,
            });
            child.material = material;
          }
        });
          this.mesh = fbx;
          this.mesh.scale.set(0.005,0.005,0.005);
          this.mesh.position.set(0, -6.4, 0);

          this.scene.add(this.mesh);
          //console.log(this.mesh);
        }, undefined, function (error) {
          console.error('Error loading FBX model:', error);
        });
  }

}
