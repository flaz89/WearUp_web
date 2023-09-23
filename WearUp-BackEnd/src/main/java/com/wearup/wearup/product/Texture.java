package com.wearup.wearup.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Texture {
	
	@Column(nullable = false)
	private String albedoMap;
    private String alphaMap;
    private String heightMap;
    private String aoMap;
    private String normalMap;
    private String metalnessMap;
    private String roughnessMap;
    
    public Texture(String albedoMap) {
        this.albedoMap = albedoMap;
    }
}
