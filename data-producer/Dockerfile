FROM python:3

ADD kafka-book-data-producer.py /
ADD isbns.txt /

RUN pip install kafka-python
RUN pip install Faker

CMD [ "python", "./kafka-book-data-producer.py" ]