package moe.pine.gravatar;

import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;

public class Command {
    public static void main(String[] args) {
        Gravatar gravatar = Gravatar.builder()
            .email("")
            .password("")
            .build();

        System.out.println("Your gravatar images");
        System.out.println("---------------------");
        System.out.println();

        UserImage[] userImages = gravatar.getUserImages();
        for (int i = 0; i < userImages.length; ++i) {
            if (i > 0) {
                System.out.println("--");
            }
            System.out.printf("Hash: %s\n", userImages[i].getHash());
            System.out.printf("URL : %s\n", userImages[i].getUrl());
        }
    }
}
