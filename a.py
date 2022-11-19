
class person():
    def say_name(self):
        print (self.name)


jane = person()
jane.name = 'jane'

bill = person()
bill.name = 'person'

bill.say_name = jane.say_name

bill.say_name()