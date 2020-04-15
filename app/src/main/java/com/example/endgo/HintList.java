package com.example.endgo;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for managing a list of hints
 * Now we load in some dummy hint items, in the future we want to load
 * in hints from a database
 */
public class HintList {

    /**
     * An array of sample (hint) items.
     */
    public static final List<ListItem> ITEMS = new ArrayList<ListItem>();

    // amount of items in the list
    private static final int COUNT = 5;

    static {
        // Add some sample items for now, eventually want to load in from database
        for (int i = 1; i <= COUNT; i++) {
            addItem(createHintItem(i));
        }
    }

    // some methods to handle the creation of hintitems
    // TODO: modify these when we can actually load in info from the server
    private static void addItem(ListItem item) {
        ITEMS.add(item);
    }

    private static ListItem createHintItem(int position) {
        return new ListItem("Hint " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A class representing an item used in a scrollable list. Contains information such as
     * the id and content of the item
     */
    public static class ListItem {
        // the name that will be shown in the hint menu
        public final String name;
        // the text of the hint the user will see when the hint is bought
        public final String content;

        public ListItem(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public ListItem(String name, int content) {
            this.name = name;
            String str;
            switch(content) {
                case 0:
                    str = "Easy";
                    break;
                case 1:
                    str = "Medium";
                    break;
                case 2:
                    str = "Hard";
                    break;
                default:
                    str = "Extreme";

            }
            this.content = str;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
