import SwiftUI

struct KeyboardView: View {
    let onKeyPress: (String) -> Void

    let keys = [
        ["Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"],
        ["A", "S", "D", "F", "G", "H", "J", "K", "L"],
        ["Z", "X", "C", "V", "B", "N", "M"]
    ]

    var body: some View {
        VStack {
            ForEach(keys, id: \.self) { row in
                HStack {
                    ForEach(row, id: \.self) { key in
                        Button(action: {
                            onKeyPress(key)
                        }) {
                            Text(key)
                                .font(.system(size: 24))
                                .frame(width: 40, height: 40)
                                .background(Color.black)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                                .padding(4)
                        }
                    }
                }
            }

            HStack {
                Button("Space") {
                    onKeyPress(" ")
                }
                .frame(width: 120, height: 40)
                .background(Color.gray)
                .foregroundColor(.white)
                .cornerRadius(8)

                Button("Delete") {
                    onKeyPress("DEL")
                }
                .frame(width: 80, height: 40)
                .background(Color.red)
                .foregroundColor(.white)
                .cornerRadius(8)

                Button("Enter") {
                    onKeyPress("\n")
                }
                .frame(width: 80, height: 40)
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(8)
            }
        }
        .padding()
        .background(Color(UIColor.systemBackground))
    }
}
