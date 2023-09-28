import { AfterViewInit, Component, ElementRef, Input, SimpleChanges, ViewChild } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { FBXLoader} from 'three/examples/jsm/loaders/FBXLoader.js';

@Component({
  selector: 'app-canvas3-d',
  templateUrl: './canvas3-d.component.html',
  styleUrls: ['./canvas3-d.component.scss']
})
export class Canvas3DComponent implements AfterViewInit {

  @ViewChild('canvasRef') canvasRef!: ElementRef<HTMLCanvasElement>;
  @Input() containerWidth!: number;
  @Input() containerHeight!: number;

  //3d link
  @Input() model3DLink!: string;
  //textures link
  @Input() albedoMap!: string;
  @Input() alphaMap!: string;
  @Input() aoMap!: string;
  @Input() heightMap!: string;
  @Input() metalnessMap!: string;
  @Input() normalMap!: string;
  @Input() roughnessMap!: string;

  private scene!: THREE.Scene;
  private mesh!: THREE.Object3D;

  private loadedModel3DLink!: string
  private loadedAlbedoMap!: string;
  private loadedAlphaMap!: string;
  private loadedAoMap!: string;
  private loadedHeightMap!: string;
  private loadedMetalnessMap!: string;
  private loadedNormalMap!: string;
  private loadedRoughnessMap!: string;

  constructor() { }

  ngAfterViewInit(): void {
    THREE.ColorManagement.enabled = false;
    const canvas = this.canvasRef.nativeElement;

    console.log(this.model3DLink);

    //* SCENE
    this.scene = new THREE.Scene();


    // LOAD MESH
    if (this.loadedModel3DLink) {
      this.loadModel();
    }

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
    camera.position.set(0,0.6,3)

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

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['model3DLink'] && changes['model3DLink'].currentValue) ||
        (changes['albedoMap'] && changes['albedoMap'].currentValue) ||
        (changes['alphaMap'] && changes['alphaMap'].currentValue) ||
        (changes['aoMap'] && changes['aoMap'].currentValue) ||
        (changes['heightMap'] && changes['heightMap'].currentValue) ||
        (changes['metalnessMap'] && changes['metalnessMap'].currentValue) ||
        (changes['normalMap'] && changes['normalMap'].currentValue) ||
        (changes['roughnessMap'] && changes['roughnessMap'].currentValue)) {

      this.loadedModel3DLink = changes['model3DLink'] ? changes['model3DLink'].currentValue as string : this.loadedModel3DLink;
      this.loadedAlbedoMap = changes['albedoMap'] ? changes['albedoMap'].currentValue as string : this.loadedAlbedoMap;
      this.loadedAlphaMap = changes['alphaMap'] ? changes['alphaMap'].currentValue as string : this.loadedAlphaMap;
      this.loadedAoMap = changes['aoMap'] ? changes['aoMap'].currentValue as string : this.loadedAoMap;
      this.loadedHeightMap = changes['heightMap'] ? changes['heightMap'].currentValue as string : this.loadedHeightMap;
      this.loadedMetalnessMap = changes['metalnessMap'] ? changes['metalnessMap'].currentValue as string : this.loadedMetalnessMap;
      this.loadedNormalMap = changes['normalMap'] ? changes['normalMap'].currentValue as string : this.loadedNormalMap;
      this.loadedRoughnessMap = changes['roughnessMap'] ? changes['roughnessMap'].currentValue as string : this.loadedRoughnessMap;
      console.log('Albedo Map:', this.loadedAlbedoMap)

      this.loadModel();
    }
  }


loadModel():void{
  if (!this.loadedModel3DLink) {
    return;
  }

  const fbxLoader = new FBXLoader();

    fbxLoader.load(this.loadedModel3DLink, fbx => {
      fbx.traverse(child => {
        if (child instanceof THREE.Mesh) {
         //const material = new THREE.MeshStandardMaterial();

         const albedoMap = this.loadedAlbedoMap ? new THREE.TextureLoader().load(this.loadedAlbedoMap) : undefined;
         const alphaMap = this.loadedAlphaMap ? new THREE.TextureLoader().load(this.loadedAlphaMap) : undefined;
         const aoMap = this.loadedAoMap ? new THREE.TextureLoader().load(this.loadedAoMap) : undefined;
         const displacementMap = this.loadedHeightMap ? new THREE.TextureLoader().load(this.loadedHeightMap) : undefined;
         const metalnessMap = this.loadedMetalnessMap ? new THREE.TextureLoader().load(this.loadedMetalnessMap) : undefined;
         const normalMap = this.loadedNormalMap ? new THREE.TextureLoader().load(this.loadedNormalMap) : undefined;
         const roughnessMap = this.loadedRoughnessMap ? new THREE.TextureLoader().load(this.loadedRoughnessMap) : undefined;

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
        console.log(this.mesh);
      }, undefined, function (error) {
        console.error('Errore nel caricamento del modello FBX:', error);
      });
}




}
