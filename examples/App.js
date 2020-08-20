/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  StatusBar,
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  TextInput,
  Button,
  Text,
  Alert,
  BackHandler,
} from 'react-native';
import MIKA from 'react-native-mika';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: 'sbipos1',
      password: 'sdJgD2TL',
      acquirerId: '3004',
      amount: '',
      paymentReturn: '',
    };
  }

  componentDidMount = () => {
    this.checkMikaPackage();
  }

  checkMikaPackage = async () => {
    // Check if MIKA is available for Payment
    const mikaActivity = await MIKA.resolveMikaActivity();
    if (!mikaActivity) {
      Alert.alert(
        'MIKA Not Found',
        'Please install MIKA Apps first to proceed with the demo',
        [{
          text: 'Ok',
          onPress: () => BackHandler.exitApp(),
        }],
        { cancelable: false },
      );
      return;
    }
  }

  onPressSubmitPayment = async () => {
    // Setup MIKA Params
    const {
      username, password, acquirerId, amount,
    } = this.state;

    // Call MIKA Payment Activity
    const requestCode = Math.floor(Math.random() * (999 - 100 + 1) + 100);
    const response = await MIKA.startMikaPayment(
      requestCode,
      username,
      password,
      acquirerId,
      parseFloat(amount),
    );

    if (response.resultCode !== MIKA.OK) {
      /* 
        {
          "data": {
            "ERROR_CODE": null,
            "ERROR_MESSAGE": "Transaction Cancelled"
          },
          "resultCode": 0
        }
      */
      Alert.alert('Transaction Cancelled', response.data.ERROR_MESSAGE);
      this.setState({ paymentReturn: '' });
    } else {
      /*
        {
          "data": {
            "ACQUIRER_CLASS": "gopay",
            "ACQUIRER_ID": "3004",
            "AUTHORIZATION_TYPE": "GOPAY SBI Pos",
            "TRANSACTION_ALIAS": "01EEX3C0R4D03TFV8VN5F59VZP",
            "TRANSACTION_ID": "2jsYIXkXoJKCXHCKLSDUE"
          },
          "resultCode": -1
        }
        {
          "data": {
            "ACQUIRER_CLASS": "cardDebit",
            "ACQUIRER_COMPANY": "Kuma Bank",
            "ACQUIRER_ID": "3005",
            "APPROVAL_CODE": "070432",
            "AUTHORIZATION_TYPE": " Debit",
            "FIRST_SIX_LAST_FOUR_PAN": "466160*4400",
            "TRANSACTION_ALIAS": "01EEX42YQ1P8RVG10ZF71DV794",
            "TRANSACTION_ID": "2jsYbbQllKt6DMFRKxp2K"
          },
          "resultCode": -1}
      */
      this.setState({ paymentReturn: JSON.stringify(response.data) });
    }
  };

  render() {
    const {
      username, password, acquirerId, amount, paymentReturn,
    } = this.state;

    return (
      <View>
        <StatusBar barStyle="light-content" backgroundColor="#232323" />
        <SafeAreaView>
          <ScrollView
            contentInsetAdjustmentBehavior="automatic"
            keyboardShouldPersistTaps="always"
            style={styles.scrollView}>
            <View style={styles.body}>
              <TextInput
                style={styles.textInput}
                onChangeText={(input) => this.setState({ username: input })}
                value={username}
                placeholder="Username"
              />

              <TextInput
                style={styles.textInput}
                onChangeText={(input) => this.setState({ password: input })}
                value={password}
                placeholder="Password"
              />

              <TextInput
                style={styles.textInput}
                onChangeText={(input) => this.setState({ acquirerId: input })}
                value={acquirerId}
                placeholder="Acquirer ID"
              />

              <TextInput
                style={styles.textInput}
                onChangeText={(input) => this.setState({ amount: input })}
                value={amount}
                placeholder="Amount"
                keyboardType="number-pad"
              />

              <Button title="SUBMIT" onPress={this.onPressSubmitPayment} />


              <Text style={{ marginTop: 30 }}>
                {paymentReturn}
              </Text>
            </View>
          </ScrollView>
        </SafeAreaView>
      </View>
    );
  };
}

const styles = StyleSheet.create({
  scrollView: {
  },
  body: {
    padding: 25,
  },
  textInput: {
    marginBottom: 20,
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 4,
  },
});

export default App;
