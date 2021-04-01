package com.example.shopper;

import java.util.Date;

public class NotificationModel {
        private String image,icon,body;
        private boolean readed;
        private Date date;

        public NotificationModel(String image, String icon, String body, boolean readed, Date date) {
            this.image = image;
            this.icon = icon;
            this.body = body;
            this.readed = readed;
            this.date = date;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public boolean isReaded() {
            return readed;
        }

        public void setReaded(boolean readed) {
            this.readed = readed;
        }



        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

}
