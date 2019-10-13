from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers='host.docker.internal:9092',         
                                         value_serializer=str.encode)
for _ in range(100):
    producer.send('foo', value='<?xml version="1.0" encoding="utf-8" ?><book year="2000"><title>Snow Crash</title><author>Neal Stephenson</author><publisher>Spectra</publisher><isbn>345256689195</isbn><price>14.95</price></book>')

producer.close()
