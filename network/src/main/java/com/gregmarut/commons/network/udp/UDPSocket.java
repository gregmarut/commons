/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.commons.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPSocket
{
	// holds the maximum size of a UDP packet
	public static final int MAX_PACKET_SIZE = 65508;
	
	private static final String UDP_LISTENING_ACTIVITY_THREAD_NAME = "UDP Listening Activity Thread";
	private static final String EVENT_DISPATCH_THREAD_NAME = "UDP Event Dispatch Thread";
	private static final String SEND_DATA_THREAD = "UDP Send Data Thread";
	
	// ** Objects **//
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// holds the datagram socket responsible for the udp communication
	private DatagramSocket datagramSocket;
	
	// holds the list of udp listeners
	protected final List<UDPListener> udpListeners;
	
	// ** Primitives **//
	// holds the port number to operate on
	private final int port;
	
	// determines whether or not this server is already listening
	private volatile boolean listening;
	
	/**
	 * Constructs a new UDP socket
	 * 
	 * @param port
	 */
	public UDPSocket(final int port)
	{
		this.port = port;
		listening = false;
		
		// set the list of udp listeners
		udpListeners = new ArrayList<UDPListener>();
	}
	
	/**
	 * Registers a listener for this UDP socket
	 * 
	 * @param listener
	 */
	public synchronized void addUDPListener(final UDPListener listener)
	{
		// make sure the list does not already contain the listener
		if (!udpListeners.contains(listener))
		{
			// add the listener to the list
			udpListeners.add(listener);
		}
	}
	
	/**
	 * Removes a listener from the list and returns whether or not it was successful
	 * 
	 * @param listener
	 * @return boolean
	 */
	public synchronized boolean removeUDPListener(final UDPListener listener)
	{
		return udpListeners.remove(listener);
	}
	
	/**
	 * Listen for incoming UDP data
	 * 
	 * @return
	 * @throws SocketException
	 */
	public synchronized boolean listen() throws SocketException
	{
		boolean result;
		
		// make sure the server is not already listening
		if (!listening)
		{
			// make sure the datagram socket is not already set
			if (null == datagramSocket)
			{
				// assign the datagram socket
				datagramSocket = createDatagramSocket();
			}
			
			listening = true;
			
			// create a new thread to run the server activity
			Thread thread = new Thread(getUDPListeningActivity(),
				UDP_LISTENING_ACTIVITY_THREAD_NAME);
			thread.start();
			
			// this thread has started listening successfully
			result = true;
		}
		else
		{
			// this thread is not listening
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Stop the server from listening for more data
	 */
	public synchronized void stop()
	{
		listening = false;
		datagramSocket.close();
	}
	
	/**
	 * Sends data to a specific host address
	 * 
	 * @param data
	 * @param host
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws IOException
	 */
	public void sendData(final byte[] data, final String host) throws UnknownHostException, SocketException
	{
		// create the address to send the packet to
		InetAddress hostAddress = InetAddress.getByName(host);
		
		sendData(data, hostAddress);
	}
	
	/**
	 * Sends data to a specific host address
	 * 
	 * @param data
	 * @param hostAddress
	 * @throws SocketException
	 * @throws IOException
	 */
	public void sendData(final byte[] data, final InetAddress hostAddress) throws SocketException
	{
		// create a packet of data to send
		DatagramPacket packet = new DatagramPacket(data, data.length,
			hostAddress, port);
		
		sendData(packet);
	}
	
	/**
	 * Sends the packet of data
	 * 
	 * @param packet
	 * @throws SocketException
	 * @throws IOException
	 */
	public void sendData(final DatagramPacket packet) throws SocketException
	{
		// make sure the datagram socket is not already set
		if (null == datagramSocket)
		{
			// assign the datagram socket
			datagramSocket = createDatagramSocket();
		}
		
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// send the packet
					datagramSocket.send(packet);
				}
				catch (IOException e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}, SEND_DATA_THREAD);
		thread.start();
	}
	
	/**
	 * Broadcasts data on all network interfaces
	 * 
	 * @param data
	 * @throws SocketException
	 * @throws IOException
	 */
	public void broadcastData(final byte[] data) throws SocketException
	{
		// for each of the broadcast addresses
		for (InetAddress broadcastAddress : getBroadcastAddresses())
		{
			// make sure the broadcast address is not null
			if (null != broadcastAddress)
			{
				// send the data to the broadcast address
				sendData(data, broadcastAddress);
			}
		}
	}
	
	private DatagramSocket createDatagramSocket() throws SocketException
	{
		return new DatagramSocket(port);
	}
	
	protected UDPListeningActivity getUDPListeningActivity()
	{
		return new UDPListeningActivity();
	}
	
	protected class UDPListeningActivity implements Runnable
	{
		@Override
		public void run()
		{
			while (listening)
			{
				try
				{
					// create the datagram packet to receive data into
					byte[] data = new byte[MAX_PACKET_SIZE];
					final DatagramPacket packet = new DatagramPacket(data, data.length);
					
					// receive the data
					datagramSocket.receive(packet);
					
					// for each of the udp listeners
					for (final UDPListener listener : udpListeners)
					{
						// create a new event dispatch thread
						Thread thread = new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								// notify the listener that a datagram packet has arrived
								listener.packetReceived(packet);
							}
						}, EVENT_DISPATCH_THREAD_NAME);
						thread.start();
					}
				}
				catch (IOException e)
				{
					
				}
			}
		}
	}
	
	private List<InetAddress> getBroadcastAddresses() throws SocketException
	{
		// holds the list of all of the broadcast address
		List<InetAddress> broadcastAddresses = new ArrayList<InetAddress>();
		
		// get an enumeration of all of the interfaces
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		
		// while there are more interfaces
		while (interfaces.hasMoreElements())
		{
			// retrieve the next network interface
			NetworkInterface networkInterface = interfaces.nextElement();
			
			// make sure this is not the loopback interface
			if (!networkInterface.isLoopback())
			{
				// for each of the interface addresses
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
				{
					// retrieve the broadcast address for this interface
					broadcastAddresses.add(interfaceAddress.getBroadcast());
				}
			}
		}
		
		return broadcastAddresses;
	}
}
