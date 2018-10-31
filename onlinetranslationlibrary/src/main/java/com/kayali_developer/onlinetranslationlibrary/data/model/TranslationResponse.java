package com.kayali_developer.onlinetranslationlibrary.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslationResponse implements Parcelable
{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("text")
    @Expose
    private List<String> text = null;
    public final static Parcelable.Creator<TranslationResponse> CREATOR = new Creator<TranslationResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TranslationResponse createFromParcel(Parcel in) {
            return new TranslationResponse(in);
        }

        public TranslationResponse[] newArray(int size) {
            return (new TranslationResponse[size]);
        }

    }
            ;

    protected TranslationResponse(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.lang = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.text, (java.lang.String.class.getClassLoader()));
    }

    public TranslationResponse() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(lang);
        dest.writeList(text);
    }

    public int describeContents() {
        return 0;
    }

}