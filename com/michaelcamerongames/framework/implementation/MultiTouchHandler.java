package com.michaelcamerongames.framework.implementation;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.michaelcamerongames.framework.Pool;
import com.michaelcamerongames.framework.Pool.PoolObjectFactory;
import com.michaelcamerongames.framework.Input.TouchEvent;

public class MultiTouchHandler implements TouchHandler
{
	boolean[] isTouched = new boolean[20];
	int[] touchX = new int[20];
	int[] touchY = new int[20];
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX, scaleY;
	
	public MultiTouchHandler(View view, float _scaleX, float _scaleY)
	{
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>()
		{
			public TouchEvent createObject() { return new TouchEvent(); }
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = _scaleX;
		this.scaleY = _scaleY;
	}

	public boolean onTouch(View v, MotionEvent event)
	{
		synchronized (this)
		{
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerID = event.getPointerId(pointerIndex);
			TouchEvent touchEvent;
			
			switch (action)
			{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				touchEvent.pointer = pointerID;
				touchEvent.x = touchX[pointerID] = (int)(event.getX(pointerIndex) * scaleX);
				touchEvent.y = touchY[pointerID] = (int)(event.getY(pointerIndex) * scaleY);
				isTouched[pointerID] = true; 
				touchEventsBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_UP;
				touchEvent.pointer = pointerID;
				touchEvent.x = touchX[pointerID] = (int)(event.getX(pointerIndex) * scaleX);
				touchEvent.y = touchY[pointerID] = (int)(event.getY(pointerIndex) * scaleY);
				isTouched[pointerID] = false; 
				touchEventsBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_MOVE:
				int pointerCount = event.getPointerCount();
				for (int i = 0; i < pointerCount; i++)
				{
					pointerIndex = i;
					pointerID = event.getPointerId(pointerIndex);
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					touchEvent.pointer = pointerID;
					touchEvent.x = touchX[pointerID] = (int)(event.getX(pointerIndex) * scaleX);
					touchEvent.y = touchY[pointerID] = (int)(event.getY(pointerIndex) * scaleY);
					touchEventsBuffer.add(touchEvent);
				}
				break;
			}
			return true;
		}
	}

	public boolean isTouchDown(int pointer)
	{
		synchronized (this)
		{
			if (pointer < 0 || pointer >= 20)
				return false;
			else
				return isTouched[pointer];
		}
	}

	public int getTouchX(int pointer)
	{
		synchronized (this)
		{
			if (pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchX[pointer];
		}
	}

	public int getTouchY(int pointer)
	{
		synchronized (this)
		{
			if (pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchY[pointer];
		}
	}

	public List<TouchEvent> getTouchEvents()
	{
		synchronized (this)
		{
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
			{
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

}
