package com.wearup.wearup.product;

public enum Product_Type {
	    // Tops
	    TSHIRT("Tops"),
	    V_NECK_TSHIRT("Tops"),
	    POLO_SHIRT("Tops"),
	    BUTTON_DOWN_SHIRT("Tops"),
	    FLANNEL_SHIRT("Tops"),
	    DRESS_SHIRT("Tops"),
	    SLEEVELESS_SHIRT("Tops"),
	    CROP_TOP("Tops"),
	    TANK_TOP("Tops"),
	    TURTLENECK("Tops"),
	    HENLEY_SHIRT("Tops"),
	    RUGBY_SHIRT("Tops"),

	    // Bottoms
	    CHINOS("Bottoms"),
	    CARGO_PANTS("Bottoms"),
	    DRESS_PANTS("Bottoms"),
	    TRACK_PANTS("Bottoms"),
	    CAPRI_PANTS("Bottoms"),
	    SWEATPANTS("Bottoms"),
	    JOGGERS("Bottoms"),
	    SHORTS("Bottoms"),
	    BOARD_SHORTS("Bottoms"),
	    DENIM_JEANS("Bottoms"),
	    SKIRTS("Bottoms"),
	    LEGGINGS("Bottoms"),
	    PLEATED_SKIRTS("Bottoms"),
	    PENCIL_SKIRTS("Bottoms"),

	    // Dresses and Skirts
	    SUNDRESS("Dresses and Skirts"),
	    COCKTAIL_DRESS("Dresses and Skirts"),
	    EVENING_GOWN("Dresses and Skirts"),
	    SHIFT_DRESS("Dresses and Skirts"),
	    A_LINE_DRESS("Dresses and Skirts"),
	    WRAP_DRESS("Dresses and Skirts"),
	    BODYCON_DRESS("Dresses and Skirts"),

	    // Jackets and Coats
	    BLAZER("Jackets and Coats"),
	    BOMBER_JACKET("Jackets and Coats"),
	    LEATHER_JACKET("Jackets and Coats"),
	    DENIM_JACKET("Jackets and Coats"),
	    TRENCH_COAT("Jackets and Coats"),
	    PEA_COAT("Jackets and Coats"),
	    PARKA("Jackets and Coats"),
	    ANORAK("Jackets and Coats"),
	    RAINCOAT("Jackets and Coats"),

	    // Knitwear
	    PULLOVER_SWEATER("Knitwear"),
	    CARDIGAN("Knitwear"),
	    HOODIE("Knitwear"),
	    ZIP_UP_FLEECE("Knitwear"),
	    VEST("Knitwear"),

	    // Footwear
	    SNEAKERS("Footwear"),
	    DRESS_SHOES("Footwear"),
	    LOAFERS("Footwear"),
	    BOOTS("Footwear"),
	    SANDALS("Footwear"),
	    FLIP_FLOPS("Footwear"),
	    HIGH_HEELS("Footwear"),
	    FLATS("Footwear"),
	    ATHLETIC_SHOES("Footwear"),

	    // Accessories
	    BELTS("Accessories"),
	    TIES("Accessories"),
	    BOW_TIES("Accessories"),
	    GLOVES("Accessories"),
	    SCARVES("Accessories"),
	    HATS("Accessories"),
	    SOCKS("Accessories"),
	    TIGHTS("Accessories"),
	    STOCKINGS("Accessories"),

	    // Underwear
	    BRIEFS("Underwear"),
	    BOXERS("Underwear"),
	    BOXER_BRIEFS("Underwear"),
	    BIKINI("Underwear"),
	    THONG("Underwear"),
	    BRA("Underwear"),
	    SPORTS_BRA("Underwear"),
	    PANTYHOSE("Underwear"),

	    // Sportswear and Casual
	    TRACKSUITS("Sportswear and Casual"),
	    YOGA_PANTS("Sportswear and Casual"),
	    RASH_GUARDS("Sportswear and Casual"),
	    SWIM_TRUNKS("Sportswear and Casual"),
	    ONE_PIECE_SWIMSUIT("Sportswear and Casual"),
	    LEGGINS("Sportswear and Casual"),

	    // Nightwear
	    PAJAMAS("Nightwear"),
	    NIGHTGOWN("Nightwear"),
	    ROBE("Nightwear");

	    private String category;

	    Product_Type(String category) {
	        this.category = category;
	    }

	    public String getCategory() {
	        return this.category;
	    }
	}

