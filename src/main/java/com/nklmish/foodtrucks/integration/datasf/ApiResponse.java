package com.nklmish.foodtrucks.integration.datasf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
    public class ApiResponse {
        private String address;
        private String applicant;
        private String schedule;
        private String status;
        private String dayshours;
        private String fooditems;
        private String locationdescription;
        private double latitude;
        private double longitude;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getApplicant() {
            return applicant;
        }

        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDayshours() {
            return dayshours;
        }

        public void setDayshours(String dayshours) {
            this.dayshours = dayshours;
        }

        public String getFooditems() {
            return fooditems;
        }

        public void setFooditems(String fooditems) {
            this.fooditems = fooditems;
        }

        public String getLocationdescription() {
            return locationdescription;
        }

        public void setLocationdescription(String locationdescription) {
            this.locationdescription = locationdescription;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "ApiResponse{" +
                    "address='" + address + '\'' +
                    ", applicant='" + applicant + '\'' +
                    ", schedule='" + schedule + '\'' +
                    ", status='" + status + '\'' +
                    ", dayshours='" + dayshours + '\'' +
                    ", fooditems='" + fooditems + '\'' +
                    ", locationdescription='" + locationdescription + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }