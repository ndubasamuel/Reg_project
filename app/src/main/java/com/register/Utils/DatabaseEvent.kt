package com.register.Utils

import com.register.Repository.DatabaseEventType

class DatabaseEvent<T>(val eventType: DatabaseEventType, val value: T){}
