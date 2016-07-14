package com.adrian.android.clinicappointments.domain;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by adrian on 5/07/16.
 */
public class Util {
    private final static String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
    private Geocoder geocoder;

    public Util(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public String getAvatarUrl(String username) {
        return GRAVATAR_URL + md5(username) + "?s=64";
    }

    public String getFromLocation(double lat, double lng) {
        String result = "";
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            Address address = addresses.get(0);

            for (int i = 0; i < address.getMaxAddressLineIndex() + 1; i++) {
                result += address.getAddressLine(i);
                if (i != address.getMaxAddressLineIndex()) {
                    result += ", ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public LatLng getLocationFromAddress(String strAddress) {

        List<Address> addressList;
        LatLng latLng = null;

        try {
            addressList = this.geocoder.getFromLocationName(strAddress, 1);
            if (addressList == null || addressList.isEmpty()) {
                return null;
            }
            Address address = addressList.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getStaticMapURL(String address, String googleAPIKey) {
//        String addressWithoutSpaces = address.replace(" ", "+");
        String addressMarker = address.replace(",", "\n");
        String URL = null;
        try {
            URL = "https://maps.googleapis.com/maps/api/staticmap?center=" + URLEncoder.encode
                    (address, "UTF-8") +
                    "&zoom=17&size=600x300&maptype=roadmap&markers=color:red%7Clabel:" + URLEncoder
                    .encode(addressMarker, "UTF-8") + "&key=" + URLEncoder.encode(googleAPIKey,
                    "UTF-8")
            ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }

}
