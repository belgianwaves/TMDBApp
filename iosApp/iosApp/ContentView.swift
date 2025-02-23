import SwiftUI
import shared

struct ContentView: View {
	var body: some View {
		NavigationShared()
	}
}

struct NavigationShared: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return SharedViewControllers().navigationViewController(
            onOpenYoutube: { movie in
                let videoId = movie.videos.split(separator: ", ")[0]
                
                let appUrl = URL(string: "youtube://\(videoId)")!
                let webUrl = URL(string: "https://www.youtube.com/watch?v=\(videoId)")!
                
                if UIApplication.shared.canOpenURL(appUrl) {
                    UIApplication.shared.open(appUrl)
                } else {
                    UIApplication.shared.open(webUrl)
                }
            },
            onOpenBrowser: { movie in
                let url = URL(string: movie.homepage!)!
                if UIApplication.shared.canOpenURL(url) {
                    UIApplication.shared.open(url)
                }
            }
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
