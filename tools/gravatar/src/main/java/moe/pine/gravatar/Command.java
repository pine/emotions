package moe.pine.gravatar;

import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.GravatarException;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Command {
    public static void main(String[] args) {

        // ----- Check environment variables

        final String email = System.getenv("GRAVATAR_EMAIL");
        final String password = System.getenv("GRAVATAR_PASSWORD");

        if (StringUtils.isEmpty(email)) {
            System.err.println("A env variable `GRAVATAR_EMAIL` should be required.");
            System.exit(1);
        }
        if (StringUtils.isEmpty(password)) {
            System.err.println("A env variable `GRAVATAR_PASSWORD` should be required.");
            System.exit(1);
        }


        // ----- Fetch gravatar avatars

        final GravatarClient gravatarClient = new GravatarClient(email);
        final Gravatar gravatar = new Gravatar(gravatarClient, password);
        final List<UserImage> userImages;
        try {
            userImages = gravatar.getUserImages();
        } catch (GravatarException e) {
            System.err.println("Cannot fetch your Gravatar's images.");
            System.err.println("Please re-confirm your email and password.");
            System.err.println();

            e.printStackTrace();
            System.exit(1);
            return;
        }

        System.out.printf("Found %d your Gravatar's images%n", userImages.size());
        System.out.println("-------------------------------");
        System.out.println();

        for (int i = 0; i < userImages.size(); ++i) {
            if (i > 0) {
                System.out.println("--");
            }
            System.out.printf("Hash: %s%n", userImages.get(i).getHash());
            System.out.printf("URL : %s%n", userImages.get(i).getUrl());
        }
    }
}
