package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.Openinghours;

/**
 * Created by lukas on 10.11.2016.
 *
 * Sources:
 * https://stwno.de/de/gastronomie/mensen/mensa-oth-regensburg
 * https://stwno.de/de/gastronomie/mensen/mensa-uni-regensburg
 * https://stwno.de/de/gastronomie/cafeterien/cafetaria-oth-pruefening
 */
public enum Location {
    OTH(R.string.location_oth, "HS-R-tag", new Openinghours() {
        @Nullable
        @Override
        public String getDuringLectures(@NonNull Weekday weekday) {
            switch (weekday) {
                case SATURDAY:
                    return "11:30 - 13:30";

                case SUNDAY:
                    return null;

                default:
                    return "11:00 – 14:15";
            }
        }

        @Nullable
        @Override
        public String getOutsideLecture(@NonNull Weekday weekday) {
            switch (weekday) {
                case SATURDAY:
                    return null;

                case SUNDAY:
                    return null;

                default:
                    return "11:00 - 14:00";
            }
        }
    }),
    OTH_EVENING(R.string.location_oth_evening, "HS-R-abend", new Openinghours() {
        @Nullable
        @Override
        public String getDuringLectures(@NonNull Weekday weekday) {
            switch (weekday) {
                case FRIDAY:
                    return null;

                case SATURDAY:
                    return null;

                case SUNDAY:
                    return null;

                default:
                    return "17:00 - 19:00";
            }
        }

        @Nullable
        @Override
        public String getOutsideLecture(@NonNull Weekday weekday) {
            return null;
        }
    }),

    PRUEFENING(R.string.location_pruefening, "Cafeteria-Pruefening", new Openinghours() {
        @Nullable
        @Override
        public String getDuringLectures(@NonNull Weekday weekday) {
            switch (weekday) {
                case FRIDAY:
                    return "07:30 - 14:00";

                case SATURDAY:
                    return null;

                case SUNDAY:
                    return null;

                default:
                    return "07:30 - 15:00";
            }
        }

        @Nullable
        @Override
        public String getOutsideLecture(@NonNull Weekday weekday) {
            return null;
        }
    }),
    UNIVERSITY(R.string.location_university, "UNI-R", new Openinghours() {
        @Nullable
        @Override
        public String getDuringLectures(@NonNull Weekday weekday) {
            switch (weekday) {
                case SATURDAY:
                    return null;

                case SUNDAY:
                    return null;

                default:
                    return "11:00 - 14:15";
            }
        }

        @Nullable
        @Override
        public String getOutsideLecture(@NonNull Weekday weekday) {
            switch (weekday) {
                case SATURDAY:
                    return null;

                case SUNDAY:
                    return null;

                default:
                    return "11:00 - 14:00";
            }
        }
    });

    @StringRes
    private final int name;
    private final String nameTag;
    private final Openinghours openinghours;

    Location(@StringRes int name, @NonNull String nameTag, @NonNull Openinghours openinghours) {
        this.name = name;
        this.nameTag = nameTag;
        this.openinghours = openinghours;
    }

    @StringRes
    public int getName() {
        return this.name;
    }

    @NonNull
    public String getNameTag() {
        return this.nameTag;
    }

    @NonNull
    public Openinghours getOpeninghours() {
        return openinghours;
    }
}