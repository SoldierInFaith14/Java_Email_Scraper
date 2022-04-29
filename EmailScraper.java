package emailscraper;

import java.io.*;
import java.util.*;
import javax.mail.*;

public class EmailScraper {

    public static void getServices() throws MessagingException, IOException {
        Folder folder = null;
        Store store = null;
        String email;
        String password;
        Scanner scan = new Scanner(System.in);

        try {

            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");

            System.out.print("Please enter your email address: ");
            email = scan.nextLine();
            System.out.print("Please enter your password: ");
            password = scan.nextLine();

            if (email.contains("@gmail.com")) {
                // For gmail, generate a custom app password for your account.
                store.connect("imap.gmail.com", email, password);
            }
            else if (email.contains("@outlook.com")) {
                store.connect("outlook.office365.com", email, password);
            }
            else {
                System.out.println("Invalid email address");
            }

            folder = store.getFolder("Inbox");

            folder.open(Folder.READ_ONLY);

            Message messages[] = folder.getMessages();

            System.out.println("# of Messages: " + folder.getMessageCount());
            System.out.println("# of Unread Messages: " + folder.getUnreadMessageCount());

            LinkedHashSet<String> lhsSenders = new LinkedHashSet<>();
            Address[] sender;

            for(int i = messages.length - 1; i >= 0; --i) {
                sender = messages[i].getFrom();
                for(Address addr : sender) {
                    lhsSenders.add(addr.toString());
                }
            }

            lhsSenders.stream().sorted().forEach(System.out::println);

        }
        finally {
            if(folder != null){folder.close(true);}
            if(store != null){store.close();}
        }
    }

    public static void main(String[] args) throws Exception {
        EmailScraper.getServices();
    }
}
