import SwiftUI
import shared

struct ContentView: View {
    let keys = KeyboardLogic().getKeyboardKeys()

    var body: some View {
        VStack {
            Text("Keyboard Preview")
                .font(.title)
                .padding()

            ScrollView {
                ForEach(keys, id: \.label) { key in
                    Text(key.label)
                        .padding()
                        .background(Color.gray)
                        .cornerRadius(5)
                        .padding(4)
                }
            }
        }
    }
}
