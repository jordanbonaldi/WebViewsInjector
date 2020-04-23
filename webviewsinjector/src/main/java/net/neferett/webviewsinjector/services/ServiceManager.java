/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services;

import android.content.Context;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceManager {

    /* Arg Constructor */
        private final Context context;
    /* end */

    @Delegate
    private List<LoginService> serviceList = new ArrayList<>();

    @SneakyThrows
    public void loadServices() {
        for (DefaultServices defaultService : DefaultServices.values())
            this.addService(defaultService.getLoginServiceClazz());
    }

    public void addService(Class<? extends LoginService> clazz) throws Exception {
        if (clazz.getDeclaredConstructors().length == 0)
            throw new Exception("Need to have at least one constructor taking a context as parameter");

        this.add((LoginService) clazz.getDeclaredConstructors()[0].newInstance(this.context));
    }

    @AllArgsConstructor
    @Getter
    /*
     * We are here creating a private enum because we can't resolve all Instance extending LoginService
     * by reflection with API LEVEL19
     */
    private enum DefaultServices {
        BOOKING(BookingService.class),
        GOOGLE(GoogleService.class),
        HOTELSCOM(HotelsComService.class),
        EXPEDIA(ExpediaService.class),
        TRAINLINE(TrainlineService.class),
        STRAVA(StravaService.class),
        FACEBOOK(FacebookService.class),
        TWITTER(TwitterService.class),
        INSTAGRAM(InstagramService.class);

        private final Class<? extends LoginService> loginServiceClazz;
    }
}
