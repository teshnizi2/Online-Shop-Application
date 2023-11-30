package Model.Product;

import java.util.ArrayList;

public class Category {
    private static ArrayList<Category> allCategories = new ArrayList<>();
    private String name;
    private Category parentCategory;
    private ArrayList<Category> subCategories;
    private ArrayList<String> productIds;
    private ArrayList<String> specialFeatures;

    public Category(String name) {
        this.name = name;
        subCategories = new ArrayList<>();
        productIds = new ArrayList<>();
        specialFeatures = new ArrayList<>();
        allCategories.add(this);
    }

    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
        subCategories = new ArrayList<>();
        productIds = new ArrayList<>();
        specialFeatures = new ArrayList<>();
        allCategories.add(this);
    }

    public Category() {
        this("");
    }

    public ArrayList<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(ArrayList<String> productIds) {
        this.productIds = productIds;
    }

    public ArrayList<String> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(ArrayList<String> specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public void addSpecialFeature(String specialFeature) {
        specialFeatures.add(specialFeature);
        productIds.forEach(productIds -> Product.getProductByID(productIds).addASpecialFeature(specialFeature, 0));
        for (Category subCategory : subCategories) {
            subCategory.addSpecialFeature(specialFeature);
        }
    }

    public void addProductToCategory(Product product){
        productIds.add(product.getProductId());
        Category currentCategory = this;
        while (currentCategory.parentCategory != null){
            currentCategory.parentCategory.addProductToCategory(product);
            currentCategory = parentCategory;
        }
    }

    public static void setAllCategories(ArrayList<Category> allCategories) {
        Category.allCategories = allCategories;
    }

    public static ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        productIds.forEach(x -> products.add(Product.getProductByID(x)));
        return products;
    }

    //todo: completing this:: i think its ok!
    public Category addSubCategoryWithName(String name){
        Category subCategory = new Category(name, this);
        for (String feature : specialFeatures) {
            subCategory.addSpecialFeature(feature);
        }
        subCategories.add(subCategory);
        return subCategory;
    }

    public void removeSpecialFeature(String specialFeature) {
        for (String productId : productIds) {
            Product product = Product.getProductByID(productId);
            if (product != null){
                product.removeSpecialFeature(specialFeature);
            }
        }
        specialFeatures.remove(specialFeature);
        if (parentCategory != null) {
            removeSpecialFeature(specialFeature);
        }
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void removeSubCategory(Category category) {
        subCategories.remove(category);
    }

    public static void showAllCategories(){
        for (Category category : allCategories) {
            System.out.println(category);
        }
    }

    public static Category getCategoryByName(String name){
        for (Category category : allCategories) {
            if (category.name.equalsIgnoreCase(name)){
                return category;
            }
        }
        return null;
    }

    public static void removeCategory(Category category) {
        for (String productId : category.productIds) {
            Product product = Product.getProductByID(productId);
            product.removeCategory(category);
        }
        for (Category subCategory : category.getSubCategories()) {
            Category.removeCategory(subCategory);
        }
        if (category.getParentCategory() != null) {
            category.parentCategory.removeSubCategory(category);
        }
        allCategories.remove(category);
    }

    public void removeProduct(Product product) {
        this.productIds.remove(product.getProductId());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Category: ").append(name);
        for (Category subCategory : subCategories) {
            stringBuilder.append("\n\t").append(subCategory.getName());
        }
        return stringBuilder.toString();
    }
}
