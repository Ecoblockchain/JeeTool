/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 CRoCS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cz.muni.fi.crocs.EduHoc.Serial;

import com.fazecast.jSerialComm.SerialPort;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialPortHandler {

    SerialPort port;

    private boolean silent = false;
    private boolean verbose = false;
    protected SerialPortListener listener;
    //private Generator g;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }
    /*
     public void setGenerator(Generator g) {
     this.g = g;
     }

     public Generator getGenerator() {
     return g;
     }
     */

    public void connect(String portName) throws IOException {
        port = SerialPort.getCommPort(portName);
        port.openPort();
        setSerialPortParameters();

    }

    public void listen(File file) {
      

        try {
            //add listener
            listener = new SerialPortListener(port, file);
            if (verbose) {
                listener.setVerbose();
            }
            if (silent) {
                listener.setSilent();
            }
            port.addDataListener(listener);
        } catch (IOException ex) {
            Logger.getLogger(SerialPortHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void write(File file) {
        SerialPortWriter writer = new SerialPortWriter(port, file);
        if (verbose) {
            writer.setVerbose();
        }
        if (silent) {
            writer.setSilent();
        }

        new Thread(writer).start();
    }

    public void write(File file, Long delay) {
        SerialPortWriter writer = new SerialPortWriter(port, file);
        if (verbose) {
            writer.setVerbose();
        }
        if (silent) {
            writer.setSilent();
        }
        writer.setDelay(delay);
        new Thread(writer).start();
    }

    public void closePort() {
        
            if (listener != null) {
                listener.close();
            }
            port.closePort();
        
    }

    /**
     * Sets the serial port parameters
     */
    private void setSerialPortParameters() throws IOException {
        CppDefineParser cpp = new CppDefineParser(System.getenv("EDU_HOC_HOME") + "/src/common.h");
        int baudRate = Integer.parseInt(cpp.findDefine("SERIAL_FREQUENCY"));
        port.setBaudRate(baudRate);
        //port.setComPortParameters(baudRate, SerialPort.DATABITS_8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        
    }

}
