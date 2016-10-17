Demos with Brodacast Receivers and Services
===========================================

1. StandAloneReceiver and TestBroadcastReceiver

StandAloneReceiver contains a broadcast receiver that creates a notification when it executes.
Prior to Android API level 12 all broadcast receivers could be invoked even if their processes have never been started. After API level 12, broadcast receivers  associated to custom intents (or even certain system intents), require that the process where the BR belongs to, has executed at least once (passing from the STOPPED stated to a process STARTED state). So the app that contains a broadcast receiver can contain another Activity in order to the broadcast receiver to be operational (and that activity should run at least once).
It is also possible to specify (with an additional flag in the intent) that STOPPED processes should also be considered.
TestBroadcastReceiver generates a broadcast intent to the previous broadcast receiver using a menu item. The broadcast receiver creates a notification associated with a pending intent (ACTION_DIAL) starting the dial application when clicked.

2. SimpleServiceDemo and StandAloneService

SimpleServiceDemo has an Activity that invokes a service through an intent and the starService() method. This application contains also a local service component that can be invoked that way. The Activity can also invoke another service in another application using also startService() (and an implicit intent).
The local service or the remote one are selected by a radio button.
StandAloneService contains the remote service called by the previous activity in SimpleServiceDemo.

3. StockQuoteService and StockQuoteClient

StockQuoteService contains only a service component that can be invoked through RPC style calls (binding). It defines an interface that can be obtained by bindService() from other activities. The interface methods are called using an inter-process mechanism and by marshalling the parameters and results between the processes (should be Parcelable). The interface and its methods must be described in an interface description language called AIDL (Android Interface Description Language).
StockQuoteClient is an application containing an activity that is the client of the previous service, binding and invoking its method.

IMPORTANT:

-to run the examples that do not have an activity we must go to the path of its apk and run it by the terminal with
-> after building the apk from android studio
-> abd install <file name'app-debug.apk'>.apk

