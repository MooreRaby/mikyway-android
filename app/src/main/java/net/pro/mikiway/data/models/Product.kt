package net.pro.mikiway.data.models

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId

data class Product(
    @BsonId val _id: String? = null, // Optional for new products
    val productName: String,
    val productThumb: String,
    val productDescription: String? = null, // Allow for optional descriptions
    val productSlug: String,
    val productPrice: Double,
    val productQuantity: Int,
    val productType: ProductType,
//    @BsonRepresentation(BsonType.OBJECT_ID) // Explicitly define object ID representation
    val productShop: String, // Reference to Shop model using its ID as a String
    val productAttributes: Map<String, Any>, // Generic map for attributes
    val productRatingsAverage: Double = 4.5, // Default rating
    val minRating: Double = 1.0, // Minimum allowed rating with a custom error message
    val maxRating: Double = 5.0, // Maximum allowed rating with a custom error message,
    val productVariations: List<ProductVariation> = emptyList(), // Default empty list
    val isDraft: Boolean = true, // Default draft state
    val isPublished: Boolean = false // Default unpublished state
) {

    // Custom validation logic for productRatingsAverage (optional)
    init {
        require(productRatingsAverage in minRating..maxRating) {
            "Rating must be between $minRating and $maxRating."
        }
    }

    enum class ProductType(val value: String) {
        ELECTRONIC("Electronic"),
        CLOTHING("Clothing"),
        FURNITURE("Furniture"),
        PHONE("Phone"),
        BOOK("Book"),
        COSMETIC("Cosmetic")
    }

    data class ProductVariation(
        val color: String,
        val size: String
    )
}