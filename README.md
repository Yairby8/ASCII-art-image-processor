# 🎨 ASCII Art Image Processor

## Overview
This project transforms images into ASCII art using customizable algorithms, character sets, and flexible output options. Written in Java, it supports both console and HTML outputs, interactive commands, and modular design for easy feature extension.

## Features

### 🖼️ Image Processing
- Supports JPEG/PNG loading, grayscale conversion
- Customizable resolution downsampling
- Image brightness matching to the best ASCII characters

### 🔤 ASCII Art Generation
- Multiple conversion algorithms and character selection
- Choose, add, or remove ASCII characters for conversion
- Rounding methods (up, down, abs) for precise matching
- Flexible resolution adjustment and live charset editing

### 💻 User Interaction
- Interactive shell for command execution:
  - Exit, change charset, add/remove chars, resolution control
  - Output choice: console or HTML (with Courier New font)
- Error handling for incorrect commands and unsupported formats

### 📝 Output Options
- Display ASCII art directly in console or write to an HTML file with zoom-in/out support

## API Highlights
- `SubImgCharMatcher`: Character matching by image brightness
- `AsciiArtAlgorithm`: Main conversion logic
- `Image`: Image manipulation utilities

## Skills Demonstrated
- Java OOP with modular package/design
- Image processing and conversion algorithms
- Interactive CLI/shell development
- Flexible, extensible API and custom exception handling

## Project Structure
```
src/
├── asciiart/
│ ├── AsciiArtAlgorithm.java
│ ├── Shell.java
│ ├── KeyboardInput.java
│ └── (other core classes)
├── image/
│ ├── Image.java
│ └── (image utilities)
├── imagecharmatching/
│ └── SubImgCharMatcher.java
├── asciioutput/
├── ConsoleAsciiOutput.java
└── HtmlAsciiOutput.java
README.md
```
## How to Run

1. Compile with `javac` and run `Shell.java`.
2. Use interactive shell commands to customize conversion.
3. Save your ASCII art via console or HTML as needed.


*Ready to convert any image into beautiful ASCII art!*
