package com.ov3rk1ll.kinocast.utils;

import android.util.Log;

import org.xbill.DNS.Address;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import okhttp3.Dns;

public class CustomDns implements Dns {
    private static final String TAG = CustomDns.class.getSimpleName();

    private boolean mInitialized;

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        // I'm initializing the DNS resolvers here to take advantage of this method being called in a background-thread managed by OkHttp
        init();
        return Collections.singletonList(Address.getByName(hostname));
    }

    private void init() {
        if (mInitialized) return; else mInitialized = true;

        try {



            // configure the resolvers, starting with the default ones (based on the current network connection)
            Resolver defaultResolver = Lookup.getDefaultResolver();
            Resolver cfFirstResolver = new SimpleResolver("1.1.1.1");
            // use Google's public DNS services
            Resolver googleFirstResolver = new SimpleResolver("8.8.8.8");
            Resolver googleSecondResolver = new SimpleResolver("8.8.4.4");
            // also try using Amazon
            Resolver amazonResolver = new SimpleResolver("205.251.198.30");
            Lookup.setDefaultResolver(new ExtendedResolver(new Resolver[]{
                    cfFirstResolver, googleFirstResolver, googleSecondResolver, amazonResolver, defaultResolver }));
        } catch (UnknownHostException e) {
            Log.w(TAG, "Couldn't initialize custom resolvers");
        }
    }

}