package com.kayali_developer.objectsrecognition.tensorflow;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.List;

public interface Classifier {

    class Recognition {
        private final String id;
        private final String title;

        private final Float confidence;

        Recognition(
                final String id, final String title, final Float confidence) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        Float getConfidence() {
            return confidence;
        }

        @Override
        @NonNull
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }

            return resultString.trim();
        }
    }

    List<Recognition> recognizeImage(Bitmap bitmap);

    void close();
}
