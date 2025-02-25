import SwiftUI
import shared

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      let config = Config()
#if DEBUG
      config.VERBOSE = true
#else
      config.VERBOSE = false
#endif
      config.LANGUAGE = Platform_iosKt.getCurrentLocale()
      
      DebugLog().verbose = config.VERBOSE
      Graph().doInit(driverFactory: DriverFactory())
    return true
  }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
