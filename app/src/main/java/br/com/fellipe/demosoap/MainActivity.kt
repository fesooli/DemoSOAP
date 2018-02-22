package br.com.fellipe.demosoap

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {

    private val url = "http://10.3.2.42:8080/CalculadoraWSService/CalculadoraWS?wsdl"
    private val nameSpace = "http://heiderlopes.com.br/"
    private val methodName = "calcular"
    private val soapAction = nameSpace + methodName;
    private val parametro1 = "num1"
    private val parametro2 = "num2"
    private val parametro3 = "op"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCalc.setOnClickListener({
            CallWebService().execute(edValorUm.text.toString(), edValorDois.text.toString(), spOperacoes.selectedItem.toString())
        })
    }

    inner class CallWebService: AsyncTask<String, Void, String>(){

        override fun onPostExecute(result: String?) {
            tvResultado.text = result
        }

        override fun doInBackground(vararg params: String?): String {
            var result = ""
            var soapObject = SoapObject(nameSpace, methodName)

            val number1Info = PropertyInfo()
            number1Info.name = parametro1
            number1Info.value = params[0]
            number1Info.type = Integer::class.java
            soapObject.addProperty(number1Info)

            val number2Info = PropertyInfo()
            number2Info.name = parametro2
            number2Info.value = params[1]
            number2Info.type = Integer::class.java
            soapObject.addProperty(number2Info)

            val operationInfo = PropertyInfo()
            operationInfo.name = parametro3
            operationInfo.value = params[2]
            operationInfo.type = Integer::class.java
            soapObject.addProperty(operationInfo)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.setOutputSoapObject(soapObject)

            val httpTransportSE = HttpTransportSE(url)
            try {
                httpTransportSE.call(soapAction, envelope)
                val soapPrimitive = envelope.response
                result = soapPrimitive.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                result = "ERRO"
            }
            return result
        }
    }
}
