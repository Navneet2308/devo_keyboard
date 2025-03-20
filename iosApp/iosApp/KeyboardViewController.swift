import UIKit
import SwiftUI

class KeyboardViewController: UIInputViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let keyboardView = KeyboardView { key in
            self.handleKeyPress(key)
        }

        let hostingController = UIHostingController(rootView: keyboardView)
        addChild(hostingController)
        view.addSubview(hostingController.view)
        hostingController.didMove(toParent: self)

        hostingController.view.frame = view.bounds
    }

    func handleKeyPress(_ key: String) {
        guard let textDocumentProxy = self.textDocumentProxy as? UITextDocumentProxy else {
            return
        }

        switch key {
        case "DEL":
            textDocumentProxy.deleteBackward()
        case "ENTER":
            textDocumentProxy.insertText("\n")
        default:
            textDocumentProxy.insertText(key)
        }
    }
}
