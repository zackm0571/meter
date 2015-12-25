# Meter

Fork of Google's material design live wallpaper. My goal is to add customizable, live data that can be pulled from multiple sources and devices. Currently I'm using a small Terminal command to update system statistics like CPU, fan speed, and battery temperate from my Mac. The command can be found below: 

while true; do rm stats.txt; istats >> stats.txt; curl -T stats.txt -u $USER_NAME:$PW ftp://name.host.com/stats.txt; sleep 2; done 

Meter is a data-driven wallpaper that displays the battery, wireless signal and notifications in a simple background visualization. It cycles through three visualizations and you can play with each wallpaper by tilting and moving the phone.


Meter comes with an application to enable a [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService.html) allowing the live wallpaper to listen to your notifications and displaying the third wallpaper.

&nbsp;

### **this is an [android experiment](http://androidexperiments.com)**

&nbsp;

Report any issues [here](https://github.com/googlecreativelab/meter/issues) - we love pull requests!

## License


```
Copyright 2015 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
