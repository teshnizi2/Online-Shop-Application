package graphics.products;

import graphics.SellerProfile.AddProduct;
import graphics.SellerProfile.ManageProducts;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SelectProductImage {

    public Pane mainPain;

    ArrayList<String> imageAddresses;
    boolean changePhoto = false;

    public void initialize(){
        setAvailableImages();
        showImages();
    }

    private void showImages(){
        int count = 0;
        int i = 0;
        LOOP: while (true) {
            for (int j = 0; j < 4; j++) {
                ImageView imageView = getImageImageView(imageAddresses.get(count++));
                mainPain.getChildren().add(imageView);
                imageView.setLayoutX(j * (imageView.getFitWidth() + 50) + 50);
                imageView.setLayoutY(i * (imageView.getFitHeight() + 300) + 50);
                if (count >= imageAddresses.size()){
                    break LOOP;
                }
            }
            i++;
        }
    }

    private ImageView getImageImageView(String imageAddress){
        ImageView imageView = new ImageView(new Image(imageAddress));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(175);
        imageView.setOnMouseClicked(e -> {
            if (changePhoto){
                ManageProducts.productImageAddress = imageAddress;
            }
            else {
                AddProduct.productImageAddress = imageAddress;
                AddProduct.selectImagePopUp = null;
            }
            ((Stage) mainPain.getScene().getWindow()).close();
        });
        return imageView;
    }

    private void setAvailableImages(){
        final String PATH = "file:src\\main\\resources\\Images\\products\\";
        String[] images = {"unKnown", "asusLaptop", "harryPotterBook", "phone", "ps5Controller", "sunScreen", "spiderManComic"
        , "headPhone", "orangeJuice", "nescafe", "shoe", "myBaby", "neckLace", "keyBoard"};
        imageAddresses = new ArrayList<>();
        for (String image : images) {
            imageAddresses.add(PATH + image + ".jpg");
        }
    }

    public void prepareForChangingPhoto(){
        changePhoto = true;
        initialize();
    }
}
