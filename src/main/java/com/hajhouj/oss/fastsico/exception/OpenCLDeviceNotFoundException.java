package com.hajhouj.oss.fastsico.exception;
/**
 * Exception class to represent an error when an OpenCL device is not found.
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-03
 */
public class OpenCLDeviceNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new OpenCLDeviceNotFoundException with the specified error message.
   * 
   * @param message the error message
   */
  public OpenCLDeviceNotFoundException(String message) {
    super(message);
  }
}