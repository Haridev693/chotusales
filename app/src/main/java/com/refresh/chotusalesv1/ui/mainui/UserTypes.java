package com.refresh.chotusalesv1.ui.mainui;

public enum UserTypes
{
        admin("admin"),
        user("user");

        private String name;

        /**
         * Constructs DatabaseContents.
         * @param name name of this content for using in database.
         */
        private UserTypes(String name) {
                this.name = name;
        }

        @Override
        public String toString() {
                return name;
        }
}
