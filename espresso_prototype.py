# 1 step = 1 minute

# In software company X, engineers work best when consuming
# one cup of espresso an hour.  The office espresso machine
# has a first-come-first-serve queue that applies to everyone,
# except for certain "super busy" engineers who are prioritized
# before non-super-busy ones.  Among competing super-busies
# the first-come-first-serve principle applies again.

# Please implement a simulator for this espresso machine.
# Input parameters are number of engineers, the chance that
# an engineer becomes super-busy in some unit of time, and for
# how long they stay super-busy.

# Feel free to discuss issues that could arise when choosing
# different parameters.
# You are free to choose any language and UI for the simulator
# (web, terminal, anything that works..)

import time
import random
import copy

class Util:
    @staticmethod
    def eventHappens(probability):
        r = random.uniform(1, 100)
        #print("random:" + str(r) + " < " + str(probability) + str(r < probability))
        return r < probability

class SimulationParameters:
    def __init__(self,
                 realSecondsPerStep,
                 engineerSecondsPerStep,
                 engineerCount,
                 busyProb,
                 busyCheckSeconds,
                 busySeconds,
                 secondsUntilNeedCoffee,
                 secondsUntilCoffeeReady,
                 maxQueueLengthWhenBusy):
        self.realSecondsPerStep = realSecondsPerStep
        self.engineerSecondsPerStep = engineerSecondsPerStep
        self.engineerCount = engineerCount
        self.busyProb = busyProb
        self.busySteps = round(busySeconds / engineerSecondsPerStep)
        self.stepsUntilNeedCoffee = round(secondsUntilNeedCoffee / engineerSecondsPerStep)
        self.stepsUntilCoffeeReady = round(secondsUntilCoffeeReady / engineerSecondsPerStep)
        self.maxQueueLengthWhenBusy = maxQueueLengthWhenBusy
        self.busyCheckSteps = round(busyCheckSeconds / engineerSecondsPerStep)

class CoffeeQueue:
    def __init__(self):
        self.queue = []

    def clone(self):
        c = CoffeeQueue()
        c.queue = self.queue[:]
        return c

    def next(self):
        if len(self.queue) == 0:
            return None
        for engineer in self.queue:
            if engineer["isBusy"]:
                return engineer["id"]
        return self.queue[0]["id"]

    def add(self, engineerId, isBusy):
        for engineer in self.queue:
            if engineer["id"] == engineerId:
                engineer["isBusy"] = isBusy
                return
        self.queue.append( {"id" : engineerId, "isBusy" : isBusy} )

    def update(self, engineerId, isBusy):
        for engineer in self.queue:
            if engineer["id"] == engineerId:
                engineer["isBusy"] = isBusy
                return
        raise RuntimeError("Id not found in queue: " + str(engineerId))

    def remove(self, engineerId):
        for engineer in self.queue:
            if engineer["id"] == engineerId:
                self.queue.remove(engineer)
                return

    def getIds(self):
        ids = []
        for engineer in self.queue:
            ids.append(engineer["id"])
        return ids

    def getLength(self):
        return len(self.queue)

class CoffeeMachine:
    def __init__(self):
        self.mIsIdle = True
        self.mIsCoffeeReady = False
        self.stepsUntilReady = 0
        
    def clone(self):
        c = CoffeeMachine()
        c.mIsIdle = self.mIsIdle
        c.mIsCoffeeReady = self.mIsCoffeeReady
        c.stepsUntilReady = self.stepsUntilReady
        return c
    
    def doOneStep(self):
        if not self.mIsIdle:
            if self.stepsUntilReady == 0:
                self.mIsCoffeeReady = True
                self.mIsIdle = True
            else:
                self.stepsUntilReady -= 1
    
    def isCoffeeReady(self):
        return self.mIsCoffeeReady
    
    def takeCoffee(self):
        self.mIsIdle = True
        self.mIsCoffeeReady = False
        self.stepsUntilReady = 0

    def isIdle(self):
        return self.mIsIdle
        
    def startBrewing(self, stepsUntilReady):
        self.mIsIdle = False
        self.mIsCoffeeReady = False
        self.stepsUntilReady = stepsUntilReady

    def getState(self):
        state = ""
        if self.mIsIdle:
            state = "idle "
            if self.mIsCoffeeReady:
                state += ", coffee ready"
        else:
            state = "brewing, ready in " + str(self.stepsUntilReady) + " steps"
        return state

class Engineer:
    STATE_WORKING = 1
    STATE_QUEUING = 2

    def __init__(self):
        self.id = -1
        self.state = Engineer.STATE_WORKING
        self.mIsBusy = False
        self.busySteps = 0
        self.busyCheckSteps = 0
        self.stepsUntilNeedCoffee = 0
        self.progress = None

    @staticmethod
    def make(id, parameters, progress):
        e = Engineer()
        e.id = id
        e.state = Engineer.STATE_WORKING
        e.mIsBusy = False
        e.busySteps = 0
        e.busyCheckSteps = 0
        e.stepsUntilNeedCoffee = round(random.uniform(0, 1) * parameters.stepsUntilNeedCoffee)
        e.progress = progress
        if Util.eventHappens(parameters.busyProb):
            e.mIsBusy = True
            e.busySteps = parameters.busySteps
        return e

    def clone(self):
        e = Engineer()
        e.id = self.id
        e.state = self.state
        e.mIsBusy = self.mIsBusy
        e.busySteps = self.busySteps
        e.busyCheckSteps = self.busyCheckSteps
        e.stepsUntilNeedCoffee = self.stepsUntilNeedCoffee
        e.progress = self.progress
        return e

    def getId(self):
        return self.id
    
    def isBusy(self):
        return self.mIsBusy
    
    def setBusy(self, coffeeQueue, isBusy, busySteps):
        self.mIsBusy = isBusy
        self.busySteps = busySteps
        if self.isQueuing():
            coffeeQueue.update(self.id, self.mIsBusy)
    
    def isWorking(self):
        return self.state == Engineer.STATE_WORKING

    def isQueuing(self):
        return self.state == Engineer.STATE_QUEUING

    def goForCoffee(self, coffeeQueue):
        coffeeQueue.add(self.id, self.mIsBusy)
        self.state = Engineer.STATE_QUEUING

    def goToWork(self, coffeeMachine, coffeeQueue, stepsUntilNeedCoffee):
        coffeeMachine.takeCoffee()
        coffeeQueue.remove(self.id)
        self.state = Engineer.STATE_WORKING
        self.stepsUntilNeedCoffee = stepsUntilNeedCoffee

    def doOneStep(self,
                    parameters,
                    coffeeMachine,     coffeeQueue,
                    coffeeMachineCopy, coffeeQueueCopy):
        wasBusy = self.isBusy()
        if self.isWorking():
            if self.isBusy():
                if self.busySteps == 0:
                    self.setBusy(coffeeQueueCopy, False, 0)
                    #self.progress.report(self.getState() + " becomes not-busy", coffeeMachineCopy, coffeeQueueCopy)
                else:
                    self.busySteps -= 1

            if self.stepsUntilNeedCoffee == 0:
                if (not wasBusy) or (wasBusy and coffeeQueueCopy.getLength() < parameters.maxQueueLengthWhenBusy):
                    self.goForCoffee(coffeeQueueCopy)
                    self.progress.report(self.getState() + " goes for coffee", coffeeMachineCopy, coffeeQueueCopy)
            else:
                self.stepsUntilNeedCoffee -= 1
        else:
            nextIdInQueue = coffeeQueue.next()
            if nextIdInQueue == self.id:
                if coffeeMachine.isCoffeeReady():
                    self.goToWork(coffeeMachineCopy, coffeeQueueCopy, parameters.stepsUntilNeedCoffee)
                    self.progress.report(self.getState() + " takes coffee and goes to work", coffeeMachineCopy, coffeeQueueCopy)
                elif coffeeMachine.isIdle():
                    coffeeMachineCopy.startBrewing(parameters.stepsUntilCoffeeReady)
                    self.progress.report(self.getState() + " starts brewing coffee", coffeeMachineCopy, coffeeQueueCopy)


        if not wasBusy:
            if self.busyCheckSteps == parameters.busyCheckSteps:
                if Util.eventHappens(parameters.busyProb):
                    self.setBusy(coffeeQueueCopy, True, parameters.busySteps)
                    #self.progress.report(self.getState() + " becomes busy", coffeeMachineCopy, coffeeQueueCopy)
                self.busyCheckSteps = 0
            else:
                self.busyCheckSteps += 1

    def getState(self):
        state = ""
        if self.isBusy():
            state += "{}[B,".format(self.getId())
        else:
            state += "{}[_,".format(self.getId())
        state += "{},{}]".format(self.busySteps, self.stepsUntilNeedCoffee)
        return state

class Simulation:
    def __init__(self, simulationParameters):
        self.step = 0
        self.parameters = simulationParameters
        self.coffeeMachine = CoffeeMachine()
        self.coffeeQueue = CoffeeQueue()
        self.engineers = []
        self.engineersCopy = None
        for i in range(self.parameters.engineerCount):
            engineer = Engineer.make(i, self.parameters, self)
            self.engineers.append(engineer)

    def doOneStep(self):
        coffeeMachineCopy = self.coffeeMachine.clone()
        coffeeQueueCopy = self.coffeeQueue.clone()
        self.engineersCopy = []
        for engineer in self.engineers:
            engineerCopy = engineer.clone()
            self.engineersCopy.append(engineerCopy)

        for engineerCopy in self.engineersCopy:
            engineerCopy.doOneStep(
                    self.parameters,
                    self.coffeeMachine, self.coffeeQueue,
                    coffeeMachineCopy, coffeeQueueCopy)
        coffeeMachineCopy.doOneStep()
        
        self.coffeeMachine = coffeeMachineCopy
        self.coffeeQueue = coffeeQueueCopy
        self.engineers = self.engineersCopy
        
        self.step += 1
        time.sleep(self.parameters.realSecondsPerStep)

    def getState(self, coffeeMachine = None, coffeeQueue = None):
        if coffeeMachine == None:
            coffeeMachine = self.coffeeMachine
        if coffeeQueue == None:
            coffeeQueue = self.coffeeQueue
        engineers = self.engineersCopy
        if engineers == None:
            engineers = self.engineers

        state = "Working       : "
        for engineer in engineers:
            if engineer.isWorking():
                state += engineer.getState() + " "
        state += "\nCoffee queue  : "
        for id in coffeeQueue.getIds():
            engineer = next(e for e in engineers if e.id == id)
            state += engineer.getState() + " "
        state += "\nCoffee machine: " + coffeeMachine.getState()
        return state

    def report(self, message, coffeeMachine, coffeeQueue):
        print("Step: " + str(self.step) + ", " + str(self.step * self.parameters.engineerSecondsPerStep) + " engineer seconds")
        print("Event: " + message)
        state = self.getState(coffeeMachine, coffeeQueue)
        print(state + "\n")
        #None

random.seed()
parameters = SimulationParameters(
    realSecondsPerStep = 0.0,
    engineerSecondsPerStep = 1,
    engineerCount = 30,
    busyProb = 30,
    busyCheckSeconds = 60 * 10,       # 10 min
    busySeconds = 60 * 10,            # 10 min
    secondsUntilNeedCoffee = 60 * 60, # 1 hour
    secondsUntilCoffeeReady = 30,     # 30 sec
    maxQueueLengthWhenBusy = 30)
simulation = Simulation(parameters)
print("Starting simulation")
state = simulation.getState()
print(state + "\n")
while True:
    simulation.doOneStep()
