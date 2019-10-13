FROM python:3

ADD kafka-book-data-producer.py /

RUN pip install kafka-python

CMD [ "python", "./kafka-book-data-producer.py" ]