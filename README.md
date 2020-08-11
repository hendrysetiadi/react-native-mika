
# react-native-mika

## Getting started

`$ npm install react-native-mika --save`

### Mostly automatic installation

`$ react-native link react-native-mika`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mika` and add `RNMika.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMika.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.hendrysetiadi.reactnative.mika.RNMikaPackage;` to the imports at the top of the file
  - Add `new RNMikaPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mika'
  	project(':react-native-mika').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mika/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mika')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNMika.sln` in `node_modules/react-native-mika/windows/RNMika.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Mika.RNMika;` to the usings at the top of the file
  - Add `new RNMikaPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNMika from 'react-native-mika';

// TODO: What to do with the module?
RNMika;
```
  