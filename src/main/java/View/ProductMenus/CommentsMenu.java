package View.ProductMenus;

import Model.Product.Comment;
import Model.Product.Product;
import View.Menu;

public class CommentsMenu extends Menu {
    private Product product;

    public CommentsMenu(Menu parentMenu, Product product) {
        super("Comments Menu", parentMenu);
        this.product = product;
    }

    @Override
    public void show() {
        for (Comment comment : productsManager.getProductComments(product)) {
            System.out.println(comment.getAccount().getName() + ": " + comment.getTitle());
            System.out.println(comment.getComment());
        }
        super.show();
    }

    private Menu getAddCommentMenu(){
        return new Menu("Add Comment", this) {
            @Override
            public void execute() {
                System.out.println("Title:");
                System.out.println("Content:");
            }
        };
    }
}
